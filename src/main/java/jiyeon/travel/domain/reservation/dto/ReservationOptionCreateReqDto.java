package jiyeon.travel.domain.reservation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReservationOptionCreateReqDto {

    @NotNull(message = "티켓 옵션은 필수입니다.")
    private final Long optionId;

    @Min(value = 1, message = "티켓 수량은 최소 1 이상이어야 합니다.")
    private final int quantity;
}
