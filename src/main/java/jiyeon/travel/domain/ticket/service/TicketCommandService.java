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
public class TicketCommandService {

    private final TicketRepository ticketRepository;

    @Transactional
    public void changeToActiveOrClosedSaleStatus() {
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
}
