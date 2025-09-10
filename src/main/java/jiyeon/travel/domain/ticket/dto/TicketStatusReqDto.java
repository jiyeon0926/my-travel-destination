package jiyeon.travel.domain.ticket.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TicketStatusReqDto {

    @NotBlank(message = "판매 상태는 필수입니다.")
    private final String saleStatus;
}
