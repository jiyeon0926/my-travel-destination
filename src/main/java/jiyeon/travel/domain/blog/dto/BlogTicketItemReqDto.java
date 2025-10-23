package jiyeon.travel.domain.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "티켓 사용 내역 등록 요청")
public class BlogTicketItemReqDto {

    @Schema(description = "예약 고유 식별자", example = "1")
    @NotNull(message = "예약 아이디는 필수입니다.")
    private final Long reservationId;
}
