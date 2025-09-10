package jiyeon.travel.domain.ticket.scheduler;

import jiyeon.travel.domain.ticket.service.TicketStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketScheduler {

    private final TicketStatusService ticketStatusService;

    public void activeSaleStatus() {
        ticketStatusService.activeSaleStatus();
    }

    public void closedSaleStatus() {
        ticketStatusService.closedSaleStatus();
    }
}
