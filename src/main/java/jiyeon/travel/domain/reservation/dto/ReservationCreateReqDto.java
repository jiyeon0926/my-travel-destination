package jiyeon.travel.domain.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "예약 신청 요청")
public class ReservationCreateReqDto {

    @Schema(description = "일정 고유 식별자")
    @NotNull(message = "티켓 일정은 필수입니다.")
    private final Long scheduleId;

    @Schema(description = "옵션이 없는 기본 티켓의 수량", example = "1")
    @Min(value = 1, message = "티켓 수량은 최소 1 이상이어야 합니다.")
    private final Integer baseQuantity;

    @Schema(description = "예약자명", example = "홍길동")
    @NotBlank(message = "예약자명은 필수입니다.")
    private final String reservationName;

    @Schema(description = "예약자 전화번호", example = "01012349876")
    private final String reservationPhone;

    @Schema(description = "옵션이 있는 티켓을 예약 시 선택한 옵션 목록")
    @Valid
    private final List<ReservationOptionCreateReqDto> options;
}
