package jiyeon.travel.domain.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "티켓 옵션 등록 요청")
public class TicketOptionCreateReqDto {

    @Schema(description = "옵션명", example = "청소년 이용권")
    @NotBlank(message = "옵션명은 필수입니다.")
    private final String name;

    @Schema(description = "옵션의 가격", example = "8000")
    private final int price;
}
