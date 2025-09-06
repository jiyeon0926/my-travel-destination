package jiyeon.travel.domain.ticket.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TicketOptionCreateReqDto {

    @NotBlank(message = "옵션명은 필수입니다.")
    private final String name;

    private final int price;
}
