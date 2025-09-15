package jiyeon.travel.domain.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReservationStatusReqDto {

    @NotBlank(message = "예약 상태는 필수입니다.")
    private final String status;
}
