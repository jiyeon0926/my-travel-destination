package jiyeon.travel.domain.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
@Schema(description = "티켓의 일정 정보")
public class TicketScheduleDto {

    @Schema(description = "티켓 일정 고유 식별자", example = "1")
    private final Long scheduleId;

    @Schema(description = "일정 활성화 여부", example = "true")
    private final Boolean isActive;

    @Schema(description = "일정 시작일자", example = "2025-09-12")
    private final LocalDate startDate;

    @Schema(description = "일정 시작시간", example = "11:00:00", type = "string")
    private final LocalTime startTime;

    @Schema(description = "초기 재고 수량", example = "10")
    private final int quantity;

    @Schema(description = "현재 남은 재고 수량", example = "5")
    private final int remainingQuantity;

    public static TicketScheduleDto from(TicketSchedule ticketSchedule) {
        return new TicketScheduleDto(
                ticketSchedule.getId(),
                ticketSchedule.isActive(),
                ticketSchedule.getStartDate(),
                ticketSchedule.getStartTime(),
                ticketSchedule.getQuantity(),
                ticketSchedule.getRemainingQuantity()
        );
    }
}
