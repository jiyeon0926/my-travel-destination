package jiyeon.travel.domain.ticket.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class TicketListResDto {

    private final Long total;
    private final List<TicketSimpleResDto> tickets;

    public TicketListResDto(Long total, List<TicketSimpleResDto> tickets) {
        this.total = total;
        this.tickets = tickets;
    }
}
