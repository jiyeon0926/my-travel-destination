package jiyeon.travel.domain.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "예약 상태 변경 요청")
public class ReservationStatusReqDto {

    @Schema(description = "예약 상태", example = "USED")
    @NotBlank(message = "예약 상태는 필수입니다.")
    private final String status;
}
