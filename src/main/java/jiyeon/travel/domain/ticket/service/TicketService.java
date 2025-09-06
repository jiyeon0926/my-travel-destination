package jiyeon.travel.domain.ticket.service;

import jiyeon.travel.domain.ticket.dto.TicketScheduleDetailsResDto;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import jiyeon.travel.domain.ticket.repository.TicketRepository;
import jiyeon.travel.domain.ticket.repository.TicketScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketScheduleRepository ticketScheduleRepository;

    @Transactional(readOnly = true)
    public TicketScheduleDetailsResDto getScheduleById(Long ticketId) {
        Ticket ticket = ticketRepository.findByIdOrElseThrow(ticketId);
        List<TicketSchedule> schedules = ticketScheduleRepository.getAllByTicketIdAndIsActiveTrueOrderByStartDateAsc(ticketId);

        return new TicketScheduleDetailsResDto(ticket, schedules);
    }
}
