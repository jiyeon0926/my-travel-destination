package jiyeon.travel.domain.reservation.scheduler;

import jiyeon.travel.domain.reservation.service.ReservationCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationScheduler {

    private final ReservationCommandService reservationCommandService;

    public void expiredReservations() {
        reservationCommandService.expireReservations();
    }
}
