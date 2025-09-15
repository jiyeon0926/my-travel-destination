package jiyeon.travel.domain.ticket.service;

import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketImage;
import jiyeon.travel.domain.ticket.repository.TicketImageRepository;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import jiyeon.travel.global.s3.dto.S3UploadDto;
import jiyeon.travel.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TicketImageService {

    private static final int IMAGE_MAX_SIZE = 5;

    private final TicketImageRepository ticketImageRepository;
    private final S3Service s3Service;

    @Transactional
    public List<TicketImage> saveImages(Ticket ticket, List<MultipartFile> files) {
        if (files.size() > IMAGE_MAX_SIZE) {
            throw new CustomException(ErrorCode.IMAGE_MAX_COUNT_EXCEEDED);
        }

        boolean isImage = files.stream()
                .allMatch(file -> Objects.requireNonNull(file.getContentType()).startsWith("image"));
        if (!isImage) {
            throw new CustomException(ErrorCode.IMAGE_ONLY_ALLOWED);
        }

        return IntStream.range(0, files.size())
                .mapToObj(i -> {
                    MultipartFile file = files.get(i);

                    try {
                        String fileName = file.getOriginalFilename();
                        String folderName = "ticket/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                        S3UploadDto s3UploadDto = s3Service.uploadFileToFolder(file, folderName);

                        boolean isMain = i == 0;
                        TicketImage image = new TicketImage(ticket, s3UploadDto.getUrl(), s3UploadDto.getKey(), fileName, isMain);

                        return ticketImageRepository.save(image);
                    } catch (IOException e) {
                        throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
                    }
                }).toList();
    }

    @Transactional
    public List<TicketImage> addImages(Ticket ticket, List<MultipartFile> files) {
        validateImageUpdateAllowed(ticket);
        int imageCount = ticketImageRepository.countByTicketId(ticket.getId());

        return (imageCount == 0)
                ? saveImages(ticket, files)
                : addImagesWithLimit(ticket, files, imageCount);
    }

    @Transactional
    public void deleteImage(String email, Long ticketId, Long imageId) {
        TicketImage ticketImage = ticketImageRepository.findByIdAndTicketIdAndEmail(imageId, ticketId, email)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_IMAGE_NOT_FOUND));

        validateImageUpdateAllowed(ticketImage.getTicket());

        if (ticketImage.isMain()) {
            throw new CustomException(ErrorCode.CANNOT_DELETE_TICKET_MAIN_IMAGE);
        }

        s3Service.deleteFile(ticketImage.getImageKey());
        ticketImageRepository.delete(ticketImage);
    }

    @Transactional
    public void deleteImagesFromS3(Long ticketId) {
        List<TicketImage> ticketImages = ticketImageRepository.findAllByTicketId(ticketId);

        if (!ticketImages.isEmpty()) {
            ticketImages.forEach(image -> s3Service.deleteFile(image.getImageKey()));
        }
    }

    @Transactional
    public TicketImage changeImageMain(String email, Long ticketId, Long imageId) {
        TicketImage ticketImage = ticketImageRepository.findByIdAndTicketIdAndEmail(imageId, ticketId, email)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_IMAGE_NOT_FOUND));

        validateImageUpdateAllowed(ticketImage.getTicket());

        if (ticketImage.isMain()) {
            throw new CustomException(ErrorCode.ALREADY_TICKET_MAIN_IMAGE);
        }

        ticketImageRepository.findByTicketIdAndIsMainTrue(ticketId)
                .ifPresent(image -> image.changeImageMain(false));

        ticketImage.changeImageMain(true);

        return ticketImage;
    }

    private void validateImageUpdateAllowed(Ticket ticket) {
        if (ticket.cannotUpdateImage()) {
            throw new CustomException(ErrorCode.TICKET_IMAGE_UPDATE_NOT_ALLOWED);
        }
    }

    private List<TicketImage> addImagesWithLimit(Ticket ticket, List<MultipartFile> files, int count) {
        if (count + files.size() > IMAGE_MAX_SIZE) {
            throw new CustomException(ErrorCode.IMAGE_MAX_COUNT_EXCEEDED);
        }

        boolean isImage = files.stream()
                .allMatch(file -> Objects.requireNonNull(file.getContentType()).startsWith("image"));
        if (!isImage) {
            throw new CustomException(ErrorCode.IMAGE_ONLY_ALLOWED);
        }

        return files.stream()
                .map(file -> {
                    try {
                        String fileName = file.getOriginalFilename();
                        String folderName = "ticket/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                        S3UploadDto s3UploadDto = s3Service.uploadFileToFolder(file, folderName);

                        TicketImage image = new TicketImage(ticket, s3UploadDto.getUrl(), s3UploadDto.getKey(), fileName, false);

                        return ticketImageRepository.save(image);
                    } catch (IOException e) {
                        throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
                    }
                }).toList();
    }
}
