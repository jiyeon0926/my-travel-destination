package jiyeon.travel.domain.ticket.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class TicketScheduleUpdateReqDto {

    private final LocalDate startDate;
    private final LocalTime startTime;
    private final Boolean isActive;

    @Min(value = 1, message = "티켓 수량은 최소 1 이상이어야 합니다.")
    private final Integer quantity;
}
