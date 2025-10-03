package jiyeon.travel.domain.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "예약 상세 응답")
public class ReservationDetailResDto {

    @Schema(description = "예약 고유 식별자", example = "1")
    private final Long id;

    @Schema(description = "티켓 고유 식별자", example = "1")
    private final Long ticketId;

    @Schema(description = "티켓 일정 고유 식별자", example = "1")
    private final Long scheduleId;

    @Schema(description = "티켓명")
    private final String ticketName;

    @Schema(description = "일정 시작일자", example = "2025-09-12")
    private final LocalDate startDate;

    @Schema(description = "일정 시작시간", example = "11:00:00")
    private final LocalTime startTime;

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

    @Schema(description = "취소일자")
    private final LocalDateTime cancelledAt;

    @Schema(description = "옵션이 있는 티켓을 예약 시 선택한 옵션 목록")
    private final List<ReservationOptionDto> options;

    @Schema(description = "생성일자")
    private final LocalDateTime createdAt;

    @Schema(description = "수정일자")
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
