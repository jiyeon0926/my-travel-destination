package jiyeon.travel.domain.ticket.service;

import jiyeon.travel.domain.blog.service.BlogQueryService;
import jiyeon.travel.domain.ticket.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketQueryFacade {

    private final TicketQueryService ticketQueryService;
    private final BlogQueryService blogQueryService;

    @Transactional(readOnly = true)
    public TicketDetailWithBlogResDto findTicketById(int page, int size, Long ticketId) {
        List<TicketBlogDto> ticketBlogDtoList = blogQueryService.findBlogsByTicketId(page, size, ticketId);

        return ticketQueryService.findTicketById(page, size, ticketId, ticketBlogDtoList);
    }

    @Transactional(readOnly = true)
    public TicketScheduleDetailsResDto getScheduleById(Long ticketId) {
        return ticketQueryService.getScheduleById(ticketId);
    }

    @Transactional(readOnly = true)
    public TicketOptionDetailsResDto getBaseOrOptionById(Long ticketId) {
        return ticketQueryService.getBaseOrOptionById(ticketId);
    }

    @Transactional(readOnly = true)
    public TicketListResDto searchTickets(int page, int size, String name) {
        return ticketQueryService.searchTickets(page, size, name);
    }
}
