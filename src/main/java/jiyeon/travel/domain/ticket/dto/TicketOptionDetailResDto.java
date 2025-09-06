package jiyeon.travel.domain.ticket.dto;

import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class TicketOptionDetailResDto {

    private final Long ticketId;
    private final String ticketName;
    private final Long optionId;
    private final String name;
    private final int price;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public TicketOptionDetailResDto(Ticket ticket, TicketOption ticketOption) {
        this.ticketId = ticket.getId();
        this.ticketName = ticket.getName();
        this.optionId = ticketOption.getId();
        this.name = ticketOption.getName();
        this.price = ticketOption.getPrice();
        this.createdAt = ticketOption.getCreatedAt();
        this.updatedAt = ticketOption.getUpdatedAt();
    }
}
