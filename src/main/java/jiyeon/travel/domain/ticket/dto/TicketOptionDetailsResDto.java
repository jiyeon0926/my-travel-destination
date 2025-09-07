package jiyeon.travel.domain.ticket.dto;

import jiyeon.travel.domain.ticket.entity.Ticket;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class TicketOptionDetailsResDto {

    private final Long ticketId;
    private final String ticketName;
    private final List<TicketOptionDto> options;

    public TicketOptionDetailsResDto(Ticket ticket, List<TicketOptionDto> options) {
        this.ticketId = ticket.getId();
        this.ticketName = ticket.getName();
        this.options = options;
    }
}
