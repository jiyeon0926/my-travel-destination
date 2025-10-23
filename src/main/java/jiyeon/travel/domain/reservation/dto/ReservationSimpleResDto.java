package jiyeon.travel.domain.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jiyeon.travel.domain.reservation.entity.Reservation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Schema(description = "예약 기본 응답")
public class ReservationSimpleResDto {

    @Schema(description = "예약 고유 식별자", example = "1")
    private final Long id;

    @Schema(description = "예약 번호", example = "yyyyMMdd-UUID")
    private final String reservationNumber;

    @Schema(description = "총 수량", example = "1")
    private final int totalQuantity;

    @Schema(description = "총 금액", example = "20000")
    private final int totalAmount;

    @Schema(description = "예약자명", example = "홍길동")
    private final String reservationName;

    @Schema(description = "예약자 전화번호", example = "01012349876")
    private final String reservationPhone;

    @Schema(description = "예약 상태", example = "UNPAID")
    private final String status;

    @Schema(description = "생성일자")
    private final LocalDateTime createdAt;

    @Schema(description = "수정일자")
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
