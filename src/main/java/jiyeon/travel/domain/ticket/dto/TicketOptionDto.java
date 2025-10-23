package jiyeon.travel.domain.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jiyeon.travel.domain.ticket.entity.TicketOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "티켓 옵션 정보")
public class TicketOptionDto {

    @Schema(description = "티켓 옵션 고유 식별자", example = "1")
    private final Long optionId;

    @Schema(description = "옵션명", example = "청소년 이용권")
    private final String name;

    @Schema(description = "옵션의 가격", example = "8000")
    private final int price;

    public static TicketOptionDto from(TicketOption ticketOption) {
        return new TicketOptionDto(
                ticketOption.getId(),
                ticketOption.getName(),
                ticketOption.getPrice()
        );
    }
}
