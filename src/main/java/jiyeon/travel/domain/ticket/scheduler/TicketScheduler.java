package jiyeon.travel.domain.ticket.scheduler;

import jiyeon.travel.domain.ticket.service.TicketCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketScheduler {

    private final TicketCommandService ticketCommandService;

    public void activeOrClosedSaleStatus() {
        ticketCommandService.changeToActiveOrClosedSaleStatus();
    }
}
