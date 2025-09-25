package jiyeon.travel.global.config;

import jiyeon.travel.domain.reservation.scheduler.ReservationScheduler;
import jiyeon.travel.domain.ticket.scheduler.TicketScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {

    private final TicketScheduler ticketScheduler;
    private final ReservationScheduler reservationScheduler;

    @Scheduled(fixedRate = 60_000)
    public void ticketActiveOrClosedStatus() {
        ticketScheduler.activeOrClosedSaleStatus();
    }

    @Scheduled(fixedRate = 60_000)
    public void deleteExpiredReservations() {
        reservationScheduler.deleteExpiredReservations();
    }
}
