package jiyeon.travel.domain.ticket.service;

import jiyeon.travel.domain.ticket.dto.TicketListResDto;
import jiyeon.travel.domain.ticket.dto.TicketOptionDetailsResDto;
import jiyeon.travel.domain.ticket.dto.TicketOptionDto;
import jiyeon.travel.domain.ticket.dto.TicketScheduleDetailsResDto;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketOption;
import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import jiyeon.travel.domain.ticket.repository.TicketRepository;
import jiyeon.travel.global.common.enums.TicketSaleStatus;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketOptionService ticketOptionService;
    private final TicketScheduleService ticketScheduleService;

    @Transactional(readOnly = true)
    public TicketListResDto searchTickets(int page, int size, String name) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return ticketRepository.searchTickets(pageable, name);
    }

    @Transactional(readOnly = true)
    public TicketScheduleDetailsResDto getScheduleById(Long ticketId) {
        Ticket ticket = ticketRepository.findByIdOrElseThrow(ticketId);
        List<TicketSchedule> schedules = ticketScheduleService.findActiveSchedulesByTicketId(ticketId);

        return new TicketScheduleDetailsResDto(ticket, schedules);
    }

    @Transactional(readOnly = true)
    public TicketOptionDetailsResDto getBaseOrOptionById(Long ticketId) {
        Ticket ticket = ticketRepository.findByIdOrElseThrow(ticketId);
        List<TicketOption> options = ticketOptionService.findOptionsByTicketId(ticketId);

        List<TicketOptionDto> optionDtos = options.isEmpty()
                ? List.of(new TicketOptionDto(null, "기본", ticket.getBasePrice()))
                : options.stream().map(TicketOptionDto::from).toList();

        return new TicketOptionDetailsResDto(ticket, optionDtos);
    }

    @Transactional
    public void activeOrClosedSaleStatus() {
        List<Ticket> tickets = ticketRepository.findAll();

        tickets.forEach(ticket -> {
            LocalDateTime now = LocalDateTime.now();

            if (ticket.isReadyStatus() && ticket.hasSaleStarted(now)) {
                ticket.changeSaleStatus(TicketSaleStatus.ACTIVE);
            }

            if (ticket.isActiveStatus() && ticket.hasSaleEnded(now)) {
                ticket.changeSaleStatus(TicketSaleStatus.CLOSED);
            }
        });
    }

    public Ticket getTicketByScheduleId(Long scheduleId) {
        return ticketRepository.findByScheduleId(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_NOT_FOUND));
    }

    public TicketOption getOptionById(Long optionId) {
        return ticketOptionService.getOptionById(optionId);
    }

    public TicketSchedule getActiveSchedule(Long scheduleId) {
        return ticketScheduleService.getActiveSchedule(scheduleId);
    }

    public List<TicketSchedule> findActiveSchedulesByTicketId(Long ticketId) {
        return ticketScheduleService.findActiveSchedulesByTicketId(ticketId);
    }
}
