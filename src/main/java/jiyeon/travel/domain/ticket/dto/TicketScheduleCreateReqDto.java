package jiyeon.travel.domain.ticket.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class TicketScheduleCreateReqDto {

    @NotNull(message = "일정 일자는 필수입니다.")
    private final LocalDate startDate;

    private final LocalTime startTime;

    @Min(value = 1, message = "티켓 수량은 최소 1 이상이어야 합니다.")
    private final int quantity;
}
