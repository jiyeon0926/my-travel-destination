package jiyeon.travel.domain.ticket.dto;

import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class TicketScheduleDetailsResDto {

    private final Long ticketId;
    private final String ticketName;
    private final List<TicketScheduleDto> schedules;

    public TicketScheduleDetailsResDto(Ticket ticket, List<TicketSchedule> schedules) {
        this.ticketId = ticket.getId();
        this.ticketName = ticket.getName();
        this.schedules = schedules.stream().map(TicketScheduleDto::from).toList();
    }
}
