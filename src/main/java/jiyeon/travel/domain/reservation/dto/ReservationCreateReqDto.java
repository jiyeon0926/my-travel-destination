package jiyeon.travel.domain.reservation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ReservationCreateReqDto {

    @NotNull(message = "티켓 일정은 필수입니다.")
    private final Long scheduleId;

    @Min(value = 1, message = "티켓 수량은 최소 1 이상이어야 합니다.")
    private final Integer baseQuantity;

    @NotBlank(message = "예약자명은 필수입니다.")
    private final String reservationName;

    private final String reservationPhone;

    @Valid
    private final List<ReservationOptionCreateReqDto> options;
}
