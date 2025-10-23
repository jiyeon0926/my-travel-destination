package jiyeon.travel.domain.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jiyeon.travel.domain.reservation.entity.ReservationOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "옵션이 있는 티켓을 예약 시 선택한 옵션")
public class ReservationOptionDto {

    @Schema(description = "예약 옵션 고유 식별자", example = "1")
    private final Long optionId;

    @Schema(description = "옵션 수량", example = "2")
    private final int quantity;

    @Schema(description = "옵션 가격", example = "5000")
    private final int unitPrice;

    @Schema(description = "총 가격", example = "10000")
    private final int totalPrice;

    public static ReservationOptionDto from(ReservationOption reservationOption) {
        return new ReservationOptionDto(
                reservationOption.getId(),
                reservationOption.getQuantity(),
                reservationOption.getUnitPrice(),
                reservationOption.getTotalPrice()
        );
    }
}
