package jiyeon.travel.domain.reservation.dto;

import jiyeon.travel.domain.reservation.entity.ReservationOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReservationOptionDto {

    private final Long optionId;
    private final int quantity;
    private final int unitPrice;
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
