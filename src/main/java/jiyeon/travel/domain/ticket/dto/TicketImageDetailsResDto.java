package jiyeon.travel.domain.ticket.dto;

import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class TicketImageDetailsResDto {

    private final Long ticketId;
    private final String ticketName;
    private final List<TicketImageDto> images;

    public TicketImageDetailsResDto(Ticket ticket, List<TicketImage> images) {
        this.ticketId = ticket.getId();
        this.ticketName = ticket.getName();
        this.images = images.stream().map(TicketImageDto::from).toList();
    }
}
