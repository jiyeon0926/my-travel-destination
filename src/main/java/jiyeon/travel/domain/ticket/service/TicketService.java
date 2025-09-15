package jiyeon.travel.domain.ticket.service;

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
    public void activeSaleStatus() {
        List<Ticket> tickets = ticketRepository.findBySaleStatus(TicketSaleStatus.READY);

        tickets.forEach(ticket -> {
            LocalDateTime now = LocalDateTime.now();
            if (ticket.hasSaleStarted(now)) {
                ticket.changeSaleStatus(TicketSaleStatus.ACTIVE);
            }
        });
    }

    @Transactional
    public void closedSaleStatus() {
        List<Ticket> tickets = ticketRepository.findBySaleStatus(TicketSaleStatus.ACTIVE);

        tickets.forEach(ticket -> {
            LocalDateTime now = LocalDateTime.now();
            if (ticket.hasSaleEnded(now)) {
                ticket.changeSaleStatus(TicketSaleStatus.CLOSED);
            }
        });
    }

    public Ticket getTicketByReservationId(Long reservationId) {
        return ticketRepository.getByReservationId(reservationId);
    }

    public Ticket getTicketByScheduleId(Long scheduleId) {
        return ticketRepository.findByScheduleId(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_NOT_FOUND));
    }
}
