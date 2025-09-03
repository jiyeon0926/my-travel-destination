package jiyeon.travel.domain.ticket.service;

import jiyeon.travel.domain.ticket.dto.TicketDetailResDto;
import jiyeon.travel.domain.ticket.dto.TicketInfoDetailResDto;
import jiyeon.travel.domain.ticket.dto.TicketOptionCreateReqDto;
import jiyeon.travel.domain.ticket.dto.TicketScheduleCreateReqDto;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketImage;
import jiyeon.travel.domain.ticket.entity.TicketOption;
import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import jiyeon.travel.domain.ticket.repository.TicketImageRepository;
import jiyeon.travel.domain.ticket.repository.TicketOptionRepository;
import jiyeon.travel.domain.ticket.repository.TicketRepository;
import jiyeon.travel.domain.ticket.repository.TicketScheduleRepository;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.domain.user.repository.UserRepository;
import jiyeon.travel.global.common.enums.TicketStatus;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TicketPartnerService {

    private final TicketRepository ticketRepository;
    private final TicketOptionRepository ticketOptionRepository;
    private final TicketScheduleRepository ticketScheduleRepository;
    private final TicketImageRepository ticketImageRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Transactional
    public TicketDetailResDto createTicket(String email, String name, LocalDateTime saleStartDate, LocalDateTime saleEndDate,
                                           String phone, String address, Integer basePrice, String description,
                                           List<TicketOptionCreateReqDto> options, List<TicketScheduleCreateReqDto> schedules,
                                           List<MultipartFile> files) {
        if (basePrice == null && (options == null || options.isEmpty())) {
            throw new CustomException(ErrorCode.BASE_PRICE_EMPTY);
        }

        if (basePrice != null && options != null && !options.isEmpty()) {
            throw new CustomException(ErrorCode.BASE_PRICE_PRESENT);
        }

        User user = userRepository.findActiveByEmailOrElseThrow(email);
        Ticket ticket = Ticket.builder()
                .user(user)
                .name(name)
                .saleStartDate(saleStartDate)
                .saleEndDate(saleEndDate)
                .phone(phone)
                .address(address)
                .basePrice(basePrice)
                .description(description)
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        List<TicketOption> savedOptions = (options != null) ? saveTicketOptions(savedTicket, options) : Collections.emptyList();
        List<TicketSchedule> savedSchedules = (schedules != null) ? saveTicketSchedules(savedTicket, schedules) : Collections.emptyList();
        List<TicketImage> savedTicketImages = (files != null) ? saveTicketImages(savedTicket, files) : Collections.emptyList();

        return new TicketDetailResDto(savedTicket, savedOptions, savedSchedules, savedTicketImages);
    }

    @Transactional
    public void deleteTicketById(Long ticketId, String email) {
        Ticket ticket = ticketRepository.findByIdAndEmailWithUserAndOption(ticketId, email)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_NOT_FOUND));

        List<TicketImage> ticketImages = ticketImageRepository.findAllByTicketId(ticketId);
        if (!ticketImages.isEmpty()) {
            ticketImages.forEach(image -> s3Service.deleteFile(image.getImageKey()));
        }

        ticketRepository.delete(ticket);
    }

    @Transactional
    public TicketInfoDetailResDto updateTicketInfoById(Long ticketId, String email, String name,
                                                       LocalDateTime saleStartDate, LocalDateTime saleEndDate, String phone,
                                                       String address, Integer basePrice, String description) {
        Ticket ticket = ticketRepository.findByIdAndEmailWithUserAndOption(ticketId, email)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_NOT_FOUND));

        if (ticket.getStatus().equals(TicketStatus.READY) || ticket.getStatus().equals(TicketStatus.INACTIVE)) {
            boolean isOption = ticket.getTicketOptions().stream().anyMatch(option -> option.getName() != null);
            if (basePrice != null && isOption) {
                throw new CustomException(ErrorCode.TICKET_OPTION_PRESENT);
            }

            ticket.updateTicketInfo(name, saleStartDate, saleEndDate, basePrice, phone, address, description);
        }

        return new TicketInfoDetailResDto(ticket);
    }

    private List<TicketOption> saveTicketOptions(Ticket ticket, List<TicketOptionCreateReqDto> options) {
        List<TicketOption> optionList = options.stream()
                .map(option -> new TicketOption(ticket, option.getName(), option.getPrice()))
                .toList();

        return ticketOptionRepository.saveAll(optionList);
    }

    private List<TicketSchedule> saveTicketSchedules(Ticket ticket, List<TicketScheduleCreateReqDto> schedules) {
        List<TicketSchedule> scheduleList = schedules.stream()
                .map(schedule -> new TicketSchedule(ticket, schedule.getStartDate(), schedule.getStartTime(), schedule.getQuantity()))
                .toList();

        return ticketScheduleRepository.saveAll(scheduleList);
    }

    private List<TicketImage> saveTicketImages(Ticket ticket, List<MultipartFile> files) {
        int maxSize = 5;
        if (files.size() > maxSize) {
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
}
