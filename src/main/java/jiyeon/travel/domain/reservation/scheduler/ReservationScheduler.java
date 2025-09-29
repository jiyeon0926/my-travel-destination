package jiyeon.travel.domain.reservation.scheduler;

import jiyeon.travel.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationScheduler {

    private final ReservationService reservationService;

    public void expiredReservations() {
        reservationService.expireReservations();
    }
}
