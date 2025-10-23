package jiyeon.travel.domain.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "예약 시 선택한 옵션 요청")
public class ReservationOptionCreateReqDto {

    @Schema(description = "티켓 옵션 고유 식별자", example = "1")
    @NotNull(message = "티켓 옵션은 필수입니다.")
    private final Long optionId;

    @Schema(description = "옵션 수량", example = "1")
    @Min(value = 1, message = "티켓 수량은 최소 1 이상이어야 합니다.")
    private final int quantity;
}
