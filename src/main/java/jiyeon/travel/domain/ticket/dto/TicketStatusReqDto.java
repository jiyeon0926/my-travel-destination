package jiyeon.travel.domain.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "티켓 판매 상태 변경 요청")
public class TicketStatusReqDto {

    @Schema(description = "티켓 판매 상태", example = "INACTIVE")
    @NotBlank(message = "판매 상태는 필수입니다.")
    private final String saleStatus;
}
