package jiyeon.travel.domain.ticket.service;

import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.domain.reservation.service.ReservationService;
import jiyeon.travel.domain.ticket.dto.*;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketImage;
import jiyeon.travel.domain.ticket.entity.TicketOption;
import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import jiyeon.travel.domain.ticket.repository.TicketRepository;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.domain.user.service.UserService;
import jiyeon.travel.global.common.enums.TicketSaleStatus;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketPartnerService {

    private final TicketRepository ticketRepository;
    private final TicketOptionService ticketOptionService;
    private final TicketScheduleService ticketScheduleService;
    private final TicketImageService ticketImageService;
    private final UserService userService;
    private final ReservationService reservationService;

    @Transactional
    public TicketDetailResDto createTicket(String email, String name, LocalDateTime saleStartDate, LocalDateTime saleEndDate,
                                           String phone, String address, Integer basePrice, String description,
                                           List<TicketOptionCreateReqDto> options, List<TicketScheduleCreateReqDto> schedules,
                                           List<MultipartFile> files) {
        LocalDateTime now = LocalDateTime.now();
        validateSaleRange(saleStartDate, saleEndDate, now);

        if (basePrice == null && (options == null || options.isEmpty())) {
            throw new CustomException(ErrorCode.BASE_PRICE_EMPTY);
        }

        if (basePrice != null && options != null && !options.isEmpty()) {
            throw new CustomException(ErrorCode.BASE_PRICE_PRESENT);
        }

        User user = userService.getActiveUserByEmail(email);
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

        List<TicketOption> savedOptions = (options != null) ? ticketOptionService.createOptions(savedTicket, options) : Collections.emptyList();
        List<TicketSchedule> savedSchedules = (schedules != null) ? ticketScheduleService.createSchedules(savedTicket, schedules) : Collections.emptyList();
        List<TicketImage> savedTicketImages = (files != null) ? ticketImageService.saveImages(savedTicket, files) : Collections.emptyList();

        return new TicketDetailResDto(savedTicket, savedOptions, savedSchedules, savedTicketImages);
    }

    @Transactional(readOnly = true)
    public TicketListResDto findAllMyTickets(int page, int size, String email) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return ticketRepository.findAllByEmail(pageable, email);
    }

    @Transactional(readOnly = true)
    public TicketDetailResDto findMyTicketById(String email, Long ticketId) {
        Ticket ticket = ticketRepository.findByIdAndEmailOrElseThrow(ticketId, email);

        return new TicketDetailResDto(ticket, ticket.getTicketOptions(), ticket.getTicketSchedules(), ticket.getTicketImages());
    }

    @Transactional
    public void deleteTicketById(Long ticketId, String email) {
        Ticket ticket = ticketRepository.findByIdAndEmailOrElseThrow(ticketId, email);

        if (ticket.isNotReadyStatus()) {
            throw new CustomException(ErrorCode.TICKET_READY_ONLY);
        }

        ticketImageService.deleteImagesFromS3(ticketId);
        ticketRepository.delete(ticket);
    }

    @Transactional
    public TicketInfoDetailResDto updateTicketInfoById(Long ticketId, String email, String name,
                                                       LocalDateTime saleStartDate, LocalDateTime saleEndDate, String phone,
                                                       String address, Integer basePrice, String description) {
        Ticket ticket = ticketRepository.findByIdAndEmailWithUserAndOption(ticketId, email)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_NOT_FOUND));

        if (ticket.isNotReadyStatus()) {
            throw new CustomException(ErrorCode.TICKET_READY_ONLY);
        }

        LocalDateTime now = LocalDateTime.now();
        validateSaleRange(saleStartDate, saleEndDate, now);

        boolean isOption = ticket.getTicketOptions().stream().anyMatch(option -> option.getName() != null);
        if (basePrice != null && isOption) {
            throw new CustomException(ErrorCode.TICKET_OPTION_PRESENT);
        }

        if (name != null) ticket.changeName(name);
        if (saleStartDate != null) ticket.changeSaleStartDate(saleStartDate);
        if (saleEndDate != null) ticket.changeSaleEndDate(saleEndDate);
        if (basePrice != null) ticket.changeBasePrice(basePrice);
        if (phone != null) ticket.changePhone(phone);
        if (address != null) ticket.changeAddress(address);
        if (description != null) ticket.changeDescription(description);

        return new TicketInfoDetailResDto(ticket);
    }

    @Transactional
    public TicketInfoDetailResDto changeTicketStatusById(String email, Long ticketId, String saleStatus) {
        Ticket ticket = ticketRepository.findByIdAndEmailOrElseThrow(ticketId, email);
        TicketSaleStatus currentStatus = TicketSaleStatus.of(saleStatus);

        if (ticket.isSoldOutStatus() || ticket.isClosedStatus()) {
            throw new CustomException(ErrorCode.INVALID_STATUS_CHANGE);
        }

        List<Reservation> reservations = reservationService.findReservationsByTicketIdWithTicketAndSchedule(ticketId);
        boolean isPaidReservation = reservations.stream().allMatch(Reservation::isPaidStatus);

        if (ticket.isActiveStatus() && isPaidReservation) {
            throw new CustomException(ErrorCode.RESERVATION_EXISTS_INACTIVE_NOT_ALLOWED);
        }

        ticket.changeSaleStatus(currentStatus);

        return new TicketInfoDetailResDto(ticket);
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

        TicketOption ticketOption = ticketOptionService.createOption(ticket, name, price);

        return new TicketOptionDetailResDto(ticket, ticketOption);
    }

    @Transactional
    public void deleteOptionById(String email, Long ticketId, Long optionId) {
        ticketOptionService.deleteOption(email, ticketId, optionId);
    }

    @Transactional
    public TicketOptionDetailResDto updateOptionById(String email, Long ticketId, Long optionId, String name, Integer price) {
        TicketOption ticketOption = ticketOptionService.updateOption(email, ticketId, optionId, name, price);

        return new TicketOptionDetailResDto(ticketOption.getTicket(), ticketOption);
    }

    @Transactional
    public TicketScheduleDetailResDto addScheduleById(String email, Long ticketId,
                                                      LocalDate startDate, LocalTime startTime, int quantity) {
        Ticket ticket = ticketRepository.findByIdAndEmailOrElseThrow(ticketId, email);

        if (ticket.isNotReadyStatus()) {
            throw new CustomException(ErrorCode.TICKET_READY_ONLY);
        }

        TicketSchedule ticketSchedule = ticketScheduleService.createSchedule(ticket, startDate, startTime, quantity);

        return new TicketScheduleDetailResDto(ticket, ticketSchedule);
    }

    @Transactional
    public void deleteScheduleById(String email, Long ticketId, Long scheduleId) {
        ticketScheduleService.deleteSchedule(email, ticketId, scheduleId);
    }

    @Transactional
    public TicketScheduleDetailResDto updateScheduleById(String email, Long ticketId, Long scheduleId, LocalDate startDate,
                                                         LocalTime startTime, Boolean isActive, Integer quantity) {
        TicketSchedule ticketSchedule = ticketScheduleService.updateSchedule(email, ticketId, scheduleId, startDate, startTime, isActive, quantity);

        return new TicketScheduleDetailResDto(ticketSchedule.getTicket(), ticketSchedule);
    }

    @Transactional
    public TicketImageDetailsResDto addImageById(String email, Long ticketId, List<MultipartFile> files) {
        Ticket ticket = ticketRepository.findByIdAndEmailOrElseThrow(ticketId, email);
        List<TicketImage> ticketImages = ticketImageService.addImages(ticket, files);

        return new TicketImageDetailsResDto(ticket, ticketImages);
    }

    @Transactional
    public void deleteImageById(String email, Long ticketId, Long imageId) {
        ticketImageService.deleteImage(email, ticketId, imageId);
    }

    @Transactional
    public TicketImageDetailResDto changeImageMainById(String email, Long ticketId, Long imageId) {
        TicketImage ticketImage = ticketImageService.changeImageMain(email, ticketId, imageId);

        return new TicketImageDetailResDto(ticketImage);
    }

    private void validateSaleRange(LocalDateTime saleStartDate, LocalDateTime saleEndDate, LocalDateTime referenceTime) {
        if (saleStartDate.isBefore(referenceTime)) {
            throw new CustomException(ErrorCode.INVALID_SALE_START_DATE);
        }

        if (saleEndDate.isBefore(saleStartDate)) {
            throw new CustomException(ErrorCode.INVALID_SALE_END_DATE);
        }
    }
}
