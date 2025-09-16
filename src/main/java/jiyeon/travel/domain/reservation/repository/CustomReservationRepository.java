package jiyeon.travel.domain.reservation.repository;

import jiyeon.travel.domain.reservation.entity.Reservation;

import java.util.List;
import java.util.Optional;

public interface CustomReservationRepository {

    Optional<Reservation> findByIdWithTicketAndSchedule(Long id);

    List<Reservation> findAllByTicketIdWithTicketAndSchedule(Long ticketId);

    Optional<Reservation> findByIdAndPartnerEmailWithTicketAndSchedule(Long id, String email);

    List<Reservation> findAllByPartnerEmailWithoutUnpaid(String email);

    Optional<Reservation> findByIdAndUserEmailWithSchedule(Long id, String email);
}
