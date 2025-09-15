package jiyeon.travel.domain.reservation.dto;

import jiyeon.travel.domain.reservation.entity.Reservation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReservationSimpleResDto {

    private final Long id;
    private final String reservationNumber;
    private final int totalQuantity;
    private final int totalAmount;
    private final String reservationName;
    private final String reservationPhone;
    private final String status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ReservationSimpleResDto(Reservation reservation) {
        this.id = reservation.getId();
        this.reservationNumber = reservation.getReservationNumber();
        this.totalQuantity = reservation.getTotalQuantity();
        this.totalAmount = reservation.getTotalAmount();
        this.reservationName = reservation.getReservationName();
        this.reservationPhone = reservation.getReservationPhone();
        this.status = reservation.getStatus().name();
        this.createdAt = reservation.getCreatedAt();
        this.updatedAt = reservation.getUpdatedAt();
    }
}
