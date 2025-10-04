package jiyeon.travel.domain.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
@Schema(description = "티켓 일정 수정 요청")
public class TicketScheduleUpdateReqDto {

    @Schema(description = "일정 시작일자", example = "2025-09-12")
    private final LocalDate startDate;

    @Schema(description = "일정 시작시간", example = "11:00:00", type = "string")
    private final LocalTime startTime;

    @Schema(description = "일정 활성화 여부", example = "true")
    private final Boolean isActive;

    @Schema(description = "초기 재고 수량", example = "10")
    @Min(value = 1, message = "티켓 수량은 최소 1 이상이어야 합니다.")
    private final Integer quantity;
}
