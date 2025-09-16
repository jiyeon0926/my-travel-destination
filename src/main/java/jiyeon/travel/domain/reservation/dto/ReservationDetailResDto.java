package jiyeon.travel.domain.reservation.dto;

import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.domain.reservation.entity.ReservationOption;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ReservationDetailResDto {

    private final Long id;
    private final Long ticketId;
    private final Long scheduleId;
    private final String ticketName;
    private final LocalDate startDate;
    private final LocalTime startTime;
    private final String reservationNumber;
    private final int totalQuantity;
    private final int totalAmount;
    private final String reservationName;
    private final String reservationPhone;
    private final String status;
    private final LocalDateTime cancelledAt;
    private final List<ReservationOptionDto> options;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ReservationDetailResDto(Reservation reservation, Ticket ticket, TicketSchedule ticketSchedule, List<ReservationOption> options) {
        this.id = reservation.getId();
        this.ticketId = ticket.getId();
        this.scheduleId = ticketSchedule.getId();
        this.ticketName = ticket.getName();
        this.startDate = ticketSchedule.getStartDate();
        this.startTime = ticketSchedule.getStartTime();
        this.reservationNumber = reservation.getReservationNumber();
        this.totalQuantity = reservation.getTotalQuantity();
        this.totalAmount = reservation.getTotalAmount();
        this.reservationName = reservation.getReservationName();
        this.reservationPhone = reservation.getReservationPhone();
        this.status = reservation.getStatus().name();
        this.cancelledAt = reservation.getCancelledAt();
        this.options = options.stream().map(ReservationOptionDto::from).toList();
        this.createdAt = reservation.getCreatedAt();
        this.updatedAt = reservation.getUpdatedAt();
    }
}
