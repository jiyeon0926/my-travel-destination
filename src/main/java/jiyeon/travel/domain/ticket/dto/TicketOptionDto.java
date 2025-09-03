package jiyeon.travel.domain.ticket.dto;

import jiyeon.travel.domain.ticket.entity.TicketOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TicketOptionDto {

    private final Long optionId;
    private final String name;
    private final int price;

    public static TicketOptionDto from(TicketOption ticketOption) {
        return new TicketOptionDto(
                ticketOption.getId(),
                ticketOption.getName(),
                ticketOption.getPrice()
        );
    }
}
