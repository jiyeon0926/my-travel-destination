package jiyeon.travel.domain.ticket.service;

import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.repository.TicketRepository;
import jiyeon.travel.global.common.enums.TicketSaleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketStatusService {

    private final TicketRepository ticketRepository;

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
}
