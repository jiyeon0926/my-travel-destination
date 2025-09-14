package jiyeon.travel.domain.reservation.repository;

import jiyeon.travel.domain.reservation.entity.Reservation;

import java.util.Optional;

public interface CustomReservationRepository {

    Optional<Reservation> findByIdWithTicketAndSchedule(Long id);
}
