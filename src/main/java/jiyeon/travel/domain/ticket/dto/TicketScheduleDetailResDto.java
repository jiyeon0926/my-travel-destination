package jiyeon.travel.domain.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
@Schema(description = "티켓 일정 상세 응답")
public class TicketScheduleDetailResDto {

    @Schema(description = "티켓 고유 식별자", example = "1")
    private final Long ticketId;

    @Schema(description = "티켓명")
    private final String ticketName;

    @Schema(description = "티켓 일정 고유 식별자", example = "1")
    private final Long scheduleId;

    @Schema(description = "일정 활성화 여부", example = "true")
    private final Boolean isActive;

    @Schema(description = "일정 시작일자", example = "2025-09-12")
    private final LocalDate startDate;

    @Schema(description = "일정 시작시간", example = "11:00:00")
    private final LocalTime startTime;

    @Schema(description = "초기 재고 수량", example = "10")
    private final int quantity;

    @Schema(description = "현재 남은 재고 수량", example = "5")
    private final int remainingQuantity;

    @Schema(description = "생성일자")
    private final LocalDateTime createdAt;

    @Schema(description = "수정일자")
    private final LocalDateTime updatedAt;

    public TicketScheduleDetailResDto(Ticket ticket, TicketSchedule ticketSchedule) {
        this.ticketId = ticket.getId();
        this.ticketName = ticket.getName();
        this.scheduleId = ticketSchedule.getId();
        this.isActive = ticketSchedule.isActive();
        this.startDate = ticketSchedule.getStartDate();
        this.startTime = ticketSchedule.getStartTime();
        this.quantity = ticketSchedule.getQuantity();
        this.remainingQuantity = ticketSchedule.getRemainingQuantity();
        this.createdAt = ticketSchedule.getCreatedAt();
        this.updatedAt = ticketSchedule.getUpdatedAt();
    }
}
