package jiyeon.travel.domain.reservation.dto;

import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.domain.reservation.entity.ReservationOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ReservationDetailResDto {

    private final Long id;
    private final Long scheduleId;
    private final String reservationNumber;
    private final int totalQuantity;
    private final int totalAmount;
    private final String reservationName;
    private final String reservationPhone;
    private final String status;
    private final LocalDateTime cancelledAt;
    private final List<ReservationOptionDto> options;

    public ReservationDetailResDto(Reservation reservation, List<ReservationOption> options) {
        this.id = reservation.getId();
        this.scheduleId = reservation.getTicketSchedule().getId();
        this.reservationNumber = reservation.getReservationNumber();
        this.totalQuantity = reservation.getTotalQuantity();
        this.totalAmount = reservation.getTotalAmount();
        this.reservationName = reservation.getReservationName();
        this.reservationPhone = reservation.getReservationPhone();
        this.status = reservation.getStatus().name();
        this.cancelledAt = reservation.getCancelledAt();
        this.options = options.stream().map(ReservationOptionDto::from).toList();
    }
}
