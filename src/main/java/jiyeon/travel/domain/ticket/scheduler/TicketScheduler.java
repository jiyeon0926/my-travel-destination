package jiyeon.travel.domain.ticket.scheduler;

import jiyeon.travel.domain.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketScheduler {

    private final TicketService ticketService;

    public void activeSaleStatus() {
        ticketService.activeSaleStatus();
    }

    public void closedSaleStatus() {
        ticketService.closedSaleStatus();
    }
}
