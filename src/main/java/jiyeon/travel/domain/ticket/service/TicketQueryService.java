package jiyeon.travel.domain.ticket.service;

import jiyeon.travel.domain.ticket.dto.*;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketOption;
import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import jiyeon.travel.domain.ticket.repository.TicketOptionRepository;
import jiyeon.travel.domain.ticket.repository.TicketRepository;
import jiyeon.travel.domain.ticket.repository.TicketScheduleRepository;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketQueryService {

    private final TicketRepository ticketRepository;
    private final TicketOptionRepository ticketOptionRepository;
    private final TicketScheduleRepository ticketScheduleRepository;

    @Transactional(readOnly = true)
    public TicketDetailWithBlogResDto findTicketById(int page, int size, Long ticketId, List<TicketBlogDto> ticketBlogDtoList) {
        Ticket ticket = ticketRepository.findByIdWithImage(ticketId)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_NOT_FOUND));
        List<TicketOption> options = findOptionsByTicketId(ticketId);

        return new TicketDetailWithBlogResDto(ticket, options, ticket.getTicketImages(), ticketBlogDtoList);
    }

    @Transactional(readOnly = true)
    public TicketScheduleDetailsResDto getScheduleById(Long ticketId) {
        Ticket ticket = ticketRepository.findByIdOrElseThrow(ticketId);
        List<TicketSchedule> schedules = findActiveSchedulesByTicketId(ticketId);

        return new TicketScheduleDetailsResDto(ticket, schedules);
    }

    @Transactional(readOnly = true)
    public TicketOptionDetailsResDto getBaseOrOptionById(Long ticketId) {
        Ticket ticket = ticketRepository.findByIdOrElseThrow(ticketId);
        List<TicketOption> options = findOptionsByTicketId(ticketId);

        List<TicketOptionDto> optionDtoList = options.isEmpty()
                ? List.of(new TicketOptionDto(null, "기본", ticket.getBasePrice()))
                : options.stream().map(TicketOptionDto::from).toList();

        return new TicketOptionDetailsResDto(ticket, optionDtoList);
    }

    @Transactional(readOnly = true)
    public TicketListResDto searchTickets(int page, int size, String name) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return ticketRepository.searchTickets(pageable, name);
    }

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
