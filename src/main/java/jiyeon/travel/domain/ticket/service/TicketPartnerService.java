package jiyeon.travel.domain.ticket.service;

import jiyeon.travel.domain.ticket.dto.*;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TicketPartnerService {

    private static final int IMAGE_MAX_SIZE = 5;

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

        if (ticket.isReadyStatus()) {
            boolean isOption = ticket.getTicketOptions().stream().anyMatch(option -> option.getName() != null);
            if (basePrice != null && isOption) {
                throw new CustomException(ErrorCode.TICKET_OPTION_PRESENT);
            }

            if (name != null) ticket.changeName(name);
            if (saleStartDate != null) ticket.changeSaleStartDate(saleStartDate, LocalDateTime.now());
            if (saleEndDate != null) ticket.changeSaleEndDate(saleEndDate);
            if (basePrice != null) ticket.changeBasePrice(basePrice);
            if (phone != null) ticket.changePhone(phone);
            if (address != null) ticket.changeAddress(address);
            if (description != null) ticket.changeDescription(description);
        }

        return new TicketInfoDetailResDto(ticket);
    }

    @Transactional
    public void deleteOptionById(String email, Long ticketId, Long optionId) {
        TicketOption ticketOption = ticketOptionRepository.findByIdAndTicketIdAndEmail(optionId, ticketId, email)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_OPTION_NOT_FOUND));

        ticketOptionRepository.delete(ticketOption);
    }

    @Transactional
    public TicketScheduleDetailResDto addScheduleById(String email, Long ticketId,
                                                      LocalDate startDate, LocalTime startTime, int quantity) {
        Ticket ticket = ticketRepository.findByIdAndEmail(ticketId, email)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_NOT_FOUND));

        validateDuplicateDateAndTime(ticketId, startDate, startTime);
        validateScheduleBySaleDateTime(ticket, startDate, startTime);

        TicketSchedule ticketSchedule = new TicketSchedule(ticket, startDate, startTime, quantity);
        TicketSchedule savedTicketSchedule = ticketScheduleRepository.save(ticketSchedule);

        return new TicketScheduleDetailResDto(ticket, savedTicketSchedule);
    }

    @Transactional
    public void deleteScheduleById(String email, Long ticketId, Long scheduleId) {
        TicketSchedule ticketSchedule = ticketScheduleRepository.findByIdAndTicketIdAndEmail(scheduleId, ticketId, email)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_SCHEDULE_NOT_FOUND));

        ticketScheduleRepository.delete(ticketSchedule);
    }

    @Transactional
    public TicketScheduleDetailResDto updateScheduleById(String email, Long ticketId, Long scheduleId, LocalDate startDate,
                                                         LocalTime startTime, Boolean isActive, Integer quantity) {
        TicketSchedule ticketSchedule = ticketScheduleRepository.findByIdAndTicketIdAndEmail(scheduleId, ticketId, email)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_SCHEDULE_NOT_FOUND));

        validateDuplicateDateAndTime(ticketId, startDate, startTime);
        validateScheduleBySaleDateTime(ticketSchedule.getTicket(), startDate, startTime);

        if (ticketSchedule.isReadyStatus()) {
            if (isActive != null) ticketSchedule.changeIsActive(isActive);
            if (startDate != null) ticketSchedule.changeStartDate(startDate);
            if (startTime != null) ticketSchedule.changeStartTime(startTime);
            if (quantity != null) ticketSchedule.increaseQuantity(quantity);
        }

        return new TicketScheduleDetailResDto(ticketSchedule.getTicket(), ticketSchedule);
    }

    @Transactional
    public TicketImageDetailsResDto addImageById(String email, Long ticketId, List<MultipartFile> files) {
        Ticket ticket = ticketRepository.findByIdAndEmail(ticketId, email)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_NOT_FOUND));

        int imageCount = ticketImageRepository.countByTicketId(ticketId);

        List<TicketImage> savedImages = (imageCount == 0)
                ? saveTicketImages(ticket, files)
                : addTicketImages(ticket, files, imageCount);

        return new TicketImageDetailsResDto(ticket, savedImages);
    }

    @Transactional
    public void deleteImageById(String email, Long ticketId, Long imageId) {
        TicketImage ticketImage = ticketImageRepository.findByIdAndTicketIdAndEmail(imageId, ticketId, email)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_IMAGE_NOT_FOUND));

        if (ticketImage.isMain()) {
            throw new CustomException(ErrorCode.CANNOT_DELETE_TICKET_MAIN_IMAGE);
        }

        s3Service.deleteFile(ticketImage.getImageKey());
        ticketImageRepository.delete(ticketImage);
    }

    @Transactional
    public TicketImageDetailResDto changeImageMainById(String email, Long ticketId, Long imageId) {
        TicketImage ticketImage = ticketImageRepository.findByIdAndTicketIdAndEmail(imageId, ticketId, email)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_IMAGE_NOT_FOUND));

        if (ticketImage.isMain()) {
            throw new CustomException(ErrorCode.ALREADY_TICKET_MAIN_IMAGE);
        }

        ticketImageRepository.findByTicketIdAndIsMainTrue(ticketId)
                .ifPresent(image -> image.changeImageMain(false));

        ticketImage.changeImageMain(true);

        return new TicketImageDetailResDto(ticketImage);
    }

    private List<TicketOption> saveTicketOptions(Ticket ticket, List<TicketOptionCreateReqDto> options) {
        List<TicketOption> optionList = options.stream()
                .map(option -> new TicketOption(ticket, option.getName(), option.getPrice()))
                .toList();

        return ticketOptionRepository.saveAll(optionList);
    }

    private List<TicketSchedule> saveTicketSchedules(Ticket ticket, List<TicketScheduleCreateReqDto> schedules) {
        validateNullTimeSchedules(schedules);

        List<TicketSchedule> scheduleList = schedules.stream()
                .map(schedule -> {
                    LocalDate startDate = schedule.getStartDate();
                    LocalTime startTime = schedule.getStartTime();
                    validateScheduleBySaleDateTime(ticket, startDate, startTime);

                    return new TicketSchedule(ticket, schedule.getStartDate(), schedule.getStartTime(), schedule.getQuantity());
                })
                .toList();

        return ticketScheduleRepository.saveAll(scheduleList);
    }

    private void validateNullTimeSchedules(List<TicketScheduleCreateReqDto> schedules) {
        schedules.stream()
                .collect(Collectors.groupingBy(TicketScheduleCreateReqDto::getStartDate))
                .forEach((date, shceduleReqList) -> {
                    boolean hasAllDaySchedule = shceduleReqList.stream()
                            .anyMatch(schedule -> schedule.getStartTime() == null);

                    boolean hasTimedSchedule = shceduleReqList.stream()
                            .anyMatch(schedule -> schedule.getStartTime() != null);

                    if (hasAllDaySchedule && hasTimedSchedule) {
                        throw new CustomException(ErrorCode.NULL_TIME_SCHEDULE_DUPLICATE);
                    }
                });
    }

    private void validateScheduleBySaleDateTime(Ticket ticket, LocalDate startDate, LocalTime startTime) {
        LocalDate saleStartDate = ticket.getSaleStartDate().toLocalDate();
        LocalDate saleEndDate = ticket.getSaleEndDate().toLocalDate();
        LocalTime saleStartTime = ticket.getSaleStartDate().toLocalTime();
        LocalTime saleEndTime = ticket.getSaleEndDate().toLocalTime();

        if (startDate.isBefore(saleStartDate) || startDate.isAfter(saleEndDate)) {
            throw new CustomException(ErrorCode.SCHEDULE_OUT_OF_SALE_RANGE);
        }

        if (startTime != null) {
            if (startDate.isEqual(saleStartDate) && !startTime.isAfter(saleStartTime)
                    || startDate.isEqual(saleEndDate) && !startTime.isBefore(saleEndTime)) {
                throw new CustomException(ErrorCode.SCHEDULE_OUT_OF_SALE_RANGE);
            }
        }
    }

    private List<TicketImage> saveTicketImages(Ticket ticket, List<MultipartFile> files) {
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

    private void validateDuplicateDateAndTime(Long ticketId, LocalDate startDate, LocalTime startTime) {
        boolean isDate = ticketScheduleRepository.existsByTicketIdAndStartDate(ticketId, startDate);
        boolean isDateAndTime = ticketScheduleRepository.existsByTicketIdAndStartDateAndStartTime(ticketId, startDate, startTime);
        boolean isNullTime = ticketScheduleRepository.existsByTicketIdAndStartDateAndStartTimeIsNull(ticketId, startDate);

        if ((isDate && startTime == null) || isDateAndTime) {
            throw new CustomException(ErrorCode.TICKET_SCHEDULE_ALREADY_EXISTS);
        }

        if ((isNullTime && isDate)) {
            throw new CustomException(ErrorCode.NULL_TIME_SCHEDULE_DUPLICATE);
        }
    }

    private List<TicketImage> addTicketImages(Ticket ticket, List<MultipartFile> files, int count) {
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
