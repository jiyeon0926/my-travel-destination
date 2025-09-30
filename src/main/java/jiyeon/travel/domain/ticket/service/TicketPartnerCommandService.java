package jiyeon.travel.domain.ticket.service;

import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.domain.reservation.service.ReservationQueryService;
import jiyeon.travel.domain.ticket.dto.*;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketImage;
import jiyeon.travel.domain.ticket.entity.TicketOption;
import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import jiyeon.travel.domain.ticket.repository.TicketRepository;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.domain.user.service.UserQueryService;
import jiyeon.travel.global.common.enums.TicketSaleStatus;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class TicketPartnerCommandService {

    private final TicketRepository ticketRepository;
    private final TicketOptionCommandService ticketOptionCommandService;
    private final TicketScheduleCommandService ticketScheduleCommandService;
    private final TicketImageCommandService ticketImageCommandService;
    private final UserQueryService userQueryService;
    private final ReservationQueryService reservationQueryService;

    @Transactional
    public TicketDetailResDto createTicket(String email,String name, LocalDateTime saleStartDate, LocalDateTime saleEndDate,
                                           String phone, String address, Integer basePrice, String description,
                                           List<TicketOptionCreateReqDto> options, List<TicketScheduleCreateReqDto> schedules,
                                           List<MultipartFile> files) {
        User user = userQueryService.getActiveUserByEmail(email);

        LocalDateTime now = LocalDateTime.now();
        validateSaleRange(saleStartDate, saleEndDate, now);

        if (basePrice == null && (options == null || options.isEmpty())) {
            throw new CustomException(ErrorCode.BASE_PRICE_EMPTY);
        }

        if (basePrice != null && options != null && !options.isEmpty()) {
            throw new CustomException(ErrorCode.BASE_PRICE_PRESENT);
        }

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

        List<TicketOption> savedOptions = (options != null) ? ticketOptionCommandService.createOptions(savedTicket, options) : Collections.emptyList();
        List<TicketSchedule> savedSchedules = (schedules != null) ? ticketScheduleCommandService.createSchedules(savedTicket, schedules) : Collections.emptyList();
        List<TicketImage> savedTicketImages = (files != null) ? ticketImageCommandService.saveImages(savedTicket, files) : Collections.emptyList();

        return new TicketDetailResDto(savedTicket, savedOptions, savedSchedules, savedTicketImages);
    }

    @Transactional
    public TicketOptionDetailResDto addOptionById(String email, Long ticketId, String name, int price) {
        Ticket ticket = ticketRepository.findByIdAndEmailOrElseThrow(ticketId, email);

        if (ticket.isNotReadyStatus()) {
            throw new CustomException(ErrorCode.TICKET_READY_ONLY);
        }

        if (ticket.getBasePrice() != null) {
            throw new CustomException(ErrorCode.BASE_PRICE_PRESENT);
        }

        TicketOption ticketOption = ticketOptionCommandService.createOption(ticket, name, price);

        return new TicketOptionDetailResDto(ticket, ticketOption);
    }

    @Transactional
    public TicketScheduleDetailResDto addScheduleById(String email, Long ticketId,
                                                      LocalDate startDate, LocalTime startTime, int quantity) {
        Ticket ticket = ticketRepository.findByIdAndEmailOrElseThrow(ticketId, email);

        if (ticket.isNotReadyStatus()) {
            throw new CustomException(ErrorCode.TICKET_READY_ONLY);
        }

        TicketSchedule ticketSchedule = ticketScheduleCommandService.createSchedule(ticket, startDate, startTime, quantity);

        return new TicketScheduleDetailResDto(ticket, ticketSchedule);
    }

    @Transactional
    public TicketImageDetailsResDto addImageById(String email, Long ticketId, List<MultipartFile> files) {
        Ticket ticket = ticketRepository.findByIdAndEmailOrElseThrow(ticketId, email);
        List<TicketImage> ticketImages = ticketImageCommandService.addImages(ticket, files);

        return new TicketImageDetailsResDto(ticket, ticketImages);
    }

    @Transactional
    public TicketInfoDetailResDto updateTicketInfoById(Long ticketId, String email, String name,
                                                       LocalDateTime saleStartDate, LocalDateTime saleEndDate, String phone,
                                                       String address, Integer basePrice, String description) {
        Ticket ticket = ticketRepository.findByIdAndEmailWithOption(ticketId, email)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_NOT_FOUND));

        if (ticket.isNotReadyStatus()) {
            throw new CustomException(ErrorCode.TICKET_READY_ONLY);
        }

        LocalDateTime now = LocalDateTime.now();
        validateSaleRange(saleStartDate, saleEndDate, now);

        List<TicketOption> ticketOptions = ticket.getTicketOptions();
        boolean isOption = ticketOptions.stream().anyMatch(option -> option.getName() != null);
        if (basePrice != null && isOption) {
            throw new CustomException(ErrorCode.TICKET_OPTION_PRESENT);
        }

        acceptIfNotNull(name, ticket::changeName);
        acceptIfNotNull(saleStartDate, ticket::changeSaleStartDate);
        acceptIfNotNull(saleEndDate, ticket::changeSaleEndDate);
        acceptIfNotNull(basePrice, ticket::changeBasePrice);
        acceptIfNotNull(phone, ticket::changePhone);
        acceptIfNotNull(address, ticket::changeAddress);
        acceptIfNotNull(description, ticket::changeDescription);

        return new TicketInfoDetailResDto(ticket);
    }

    @Transactional
    public TicketInfoDetailResDto changeTicketStatusById(String email, Long ticketId, String saleStatus) {
        Ticket ticket = ticketRepository.findByIdAndEmailOrElseThrow(ticketId, email);
        TicketSaleStatus currentStatus = TicketSaleStatus.of(saleStatus);

        if (ticket.isSoldOutStatus() || ticket.isClosedStatus()) {
            throw new CustomException(ErrorCode.INVALID_STATUS_CHANGE);
        }

        List<Reservation> reservations = reservationQueryService.findReservationsByTicketIdWithTicketAndSchedule(ticketId);
        boolean isPaidReservation = reservations.stream().allMatch(Reservation::isPaidStatus);

        if (ticket.isActiveStatus() && isPaidReservation) {
            throw new CustomException(ErrorCode.RESERVATION_EXISTS_INACTIVE_NOT_ALLOWED);
        }

        ticket.changeSaleStatus(currentStatus);

        return new TicketInfoDetailResDto(ticket);
    }

    @Transactional
    public TicketOptionDetailResDto updateOptionById(String email, Long ticketId, Long optionId, String name, Integer price) {
        TicketOption ticketOption = ticketOptionCommandService.updateOption(email, ticketId, optionId, name, price);

        return new TicketOptionDetailResDto(ticketOption.getTicket(), ticketOption);
    }

    @Transactional
    public TicketScheduleDetailResDto updateScheduleById(String email, Long ticketId, Long scheduleId, LocalDate startDate,
                                                         LocalTime startTime, Boolean isActive, Integer quantity) {
        TicketSchedule ticketSchedule = ticketScheduleCommandService.updateSchedule(email, ticketId, scheduleId, startDate, startTime, isActive, quantity);

        return new TicketScheduleDetailResDto(ticketSchedule.getTicket(), ticketSchedule);
    }

    @Transactional
    public TicketImageDetailResDto changeImageMainById(String email, Long ticketId, Long imageId) {
        TicketImage ticketImage = ticketImageCommandService.changeImageMain(email, ticketId, imageId);

        return new TicketImageDetailResDto(ticketImage.getTicket(), ticketImage);
    }

    @Transactional
    public void deleteTicketById(Long ticketId, String email) {
        Ticket ticket = ticketRepository.findByIdAndEmailOrElseThrow(ticketId, email);

        if (ticket.isNotReadyStatus()) {
            throw new CustomException(ErrorCode.TICKET_READY_ONLY);
        }

        ticketImageCommandService.deleteImagesFromS3(ticketId);
        ticketRepository.delete(ticket);
    }

    @Transactional
    public void deleteOptionById(String email, Long ticketId, Long optionId) {
        ticketOptionCommandService.deleteOption(email, ticketId, optionId);
    }

    @Transactional
    public void deleteScheduleById(String email, Long ticketId, Long scheduleId) {
        ticketScheduleCommandService.deleteSchedule(email, ticketId, scheduleId);
    }

    @Transactional
    public void deleteImageById(String email, Long ticketId, Long imageId) {
        ticketImageCommandService.deleteImage(email, ticketId, imageId);
    }

    private void validateSaleRange(LocalDateTime saleStartDate, LocalDateTime saleEndDate, LocalDateTime referenceTime) {
        if (saleStartDate != null && saleStartDate.isBefore(referenceTime)) {
            throw new CustomException(ErrorCode.INVALID_SALE_START_DATE);
        }

        if (saleEndDate != null && saleEndDate.isBefore(saleStartDate)) {
            throw new CustomException(ErrorCode.INVALID_SALE_END_DATE);
        }
    }

    private <T> void acceptIfNotNull(T t, Consumer<T> consumer) {
        if (t != null) consumer.accept(t);
    }
}
