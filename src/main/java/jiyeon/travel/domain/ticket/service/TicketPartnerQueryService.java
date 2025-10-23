package jiyeon.travel.domain.ticket.service;

import jiyeon.travel.domain.ticket.dto.TicketDetailResDto;
import jiyeon.travel.domain.ticket.dto.TicketListResDto;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.repository.TicketRepository;
import jiyeon.travel.global.common.enums.TicketSaleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketPartnerQueryService {

    private final TicketRepository ticketRepository;

    @Transactional(readOnly = true)
    public TicketDetailResDto findMyTicketById(String email, Long ticketId) {
        Ticket ticket = ticketRepository.findByIdAndEmailOrElseThrow(ticketId, email);

        return new TicketDetailResDto(ticket, ticket.getTicketOptions(), ticket.getTicketSchedules(), ticket.getTicketImages());
    }

    @Transactional(readOnly = true)
    public TicketListResDto searchMyTickets(int page, int size, String saleStatus, String email) {
        Pageable pageable = PageRequest.of(page - 1, size);
        TicketSaleStatus ticketSaleStatus = saleStatus != null ? TicketSaleStatus.of(saleStatus) : null;

        return ticketRepository.searchMyTickets(pageable, ticketSaleStatus, email);
    }
}
