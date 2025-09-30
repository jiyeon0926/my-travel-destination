package jiyeon.travel.domain.ticket.service;

import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.domain.reservation.service.ReservationQueryService;
import jiyeon.travel.domain.ticket.dto.*;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketPartnerCommandFacade {

    private final TicketPartnerCommandService ticketPartnerCommandService;
    private final UserQueryService userQueryService;
    private final ReservationQueryService reservationQueryService;

    @Transactional
    public TicketDetailResDto createTicket(String email, String name, LocalDateTime saleStartDate, LocalDateTime saleEndDate,
                                           String phone, String address, Integer basePrice, String description,
                                           List<TicketOptionCreateReqDto> options, List<TicketScheduleCreateReqDto> schedules,
                                           List<MultipartFile> files) {
        User user = userQueryService.getActiveUserByEmail(email);

        return ticketPartnerCommandService.createTicket(name, saleStartDate, saleEndDate, phone, address, basePrice, description, options, schedules, files, user);
    }

    @Transactional
    public TicketOptionDetailResDto addOptionById(String email, Long ticketId, String name, int price) {
        return ticketPartnerCommandService.addOptionById(email, ticketId, name, price);
    }

    @Transactional
    public TicketScheduleDetailResDto addScheduleById(String email, Long ticketId,
                                                      LocalDate startDate, LocalTime startTime, int quantity) {
        return ticketPartnerCommandService.addScheduleById(email, ticketId, startDate, startTime, quantity);
    }

    @Transactional
    public TicketImageDetailsResDto addImageById(String email, Long ticketId, List<MultipartFile> files) {
        return ticketPartnerCommandService.addImageById(email, ticketId, files);
    }

    @Transactional
    public TicketInfoDetailResDto updateTicketInfoById(Long ticketId, String email, String name,
                                                       LocalDateTime saleStartDate, LocalDateTime saleEndDate, String phone,
                                                       String address, Integer basePrice, String description) {
        return ticketPartnerCommandService.updateTicketInfoById(ticketId, email, name, saleStartDate, saleEndDate, phone, address, basePrice, description);
    }

    @Transactional
    public TicketInfoDetailResDto changeTicketStatusById(String email, Long ticketId, String saleStatus) {
        List<Reservation> reservations = reservationQueryService.findReservationsByTicketIdWithTicketAndSchedule(ticketId);

        return ticketPartnerCommandService.changeTicketStatusById(email, ticketId, saleStatus, reservations);
    }

    @Transactional
    public TicketOptionDetailResDto updateOptionById(String email, Long ticketId, Long optionId, String name, Integer price) {
        return ticketPartnerCommandService.updateOptionById(email, ticketId, optionId, name, price);
    }

    @Transactional
    public TicketScheduleDetailResDto updateScheduleById(String email, Long ticketId, Long scheduleId, LocalDate startDate,
                                                         LocalTime startTime, Boolean isActive, Integer quantity) {
        return ticketPartnerCommandService.updateScheduleById(email, ticketId, scheduleId, startDate, startTime, isActive, quantity);
    }

    @Transactional
    public TicketImageDetailResDto changeImageMainById(String email, Long ticketId, Long imageId) {
        return ticketPartnerCommandService.changeImageMainById(email, ticketId, imageId);
    }

    @Transactional
    public void deleteTicketById(Long ticketId, String email) {
        ticketPartnerCommandService.deleteTicketById(ticketId, email);
    }

    @Transactional
    public void deleteOptionById(String email, Long ticketId, Long optionId) {
        ticketPartnerCommandService.deleteOptionById(email, ticketId, optionId);
    }

    @Transactional
    public void deleteScheduleById(String email, Long ticketId, Long scheduleId) {
        ticketPartnerCommandService.deleteScheduleById(email, ticketId, scheduleId);
    }

    @Transactional
    public void deleteImageById(String email, Long ticketId, Long imageId) {
        ticketPartnerCommandService.deleteImageById(email, ticketId, imageId);
    }
}
