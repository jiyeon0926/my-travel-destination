package jiyeon.travel.domain.ticket.service;

import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketOption;
import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import jiyeon.travel.domain.ticket.repository.TicketOptionRepository;
import jiyeon.travel.domain.ticket.repository.TicketRepository;
import jiyeon.travel.domain.ticket.repository.TicketScheduleRepository;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketQueryService {

    private final TicketRepository ticketRepository;
    private final TicketOptionRepository ticketOptionRepository;
    private final TicketScheduleRepository ticketScheduleRepository;

    public Ticket getTicketByScheduleId(Long scheduleId) {
        return ticketRepository.findByScheduleId(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_NOT_FOUND));
    }

    public TicketOption getOptionById(Long optionId) {
        return ticketOptionRepository.findByIdOrElseThrow(optionId);
    }

    public List<TicketOption> findOptionsByTicketId(Long ticketId) {
        return ticketOptionRepository.findAllByTicketIdOrderByPriceAsc(ticketId);
    }

    public TicketSchedule getActiveSchedule(Long scheduleId) {
        return ticketScheduleRepository.findByIdAndIsActiveTrue(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_SCHEDULE_NOT_FOUND));
    }

    public List<TicketSchedule> findActiveSchedulesByTicketId(Long ticketId) {
        return ticketScheduleRepository.findAllByTicketIdAndIsActiveTrueOrderByStartDateAsc(ticketId);
    }
}
