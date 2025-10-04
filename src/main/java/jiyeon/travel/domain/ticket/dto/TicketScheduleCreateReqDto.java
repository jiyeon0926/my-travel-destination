package jiyeon.travel.domain.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
@Schema(description = "티켓 일정 등록 요청")
public class TicketScheduleCreateReqDto {

    @Schema(description = "일정 시작일자", example = "2025-09-12")
    @NotNull(message = "일정 일자는 필수입니다.")
    private final LocalDate startDate;

    @Schema(description = "일정 시작시간", example = "11:00:00")
    private final LocalTime startTime;

    @Schema(description = "초기 재고 수량", example = "10")
    @Min(value = 1, message = "티켓 수량은 최소 1 이상이어야 합니다.")
    private final int quantity;
}
