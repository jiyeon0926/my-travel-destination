package jiyeon.travel.domain.reservation.repository;

import jiyeon.travel.domain.reservation.entity.Reservation;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomReservationRepository {

    Optional<Reservation> findByIdWithTicketAndSchedule(Long id);

    Optional<Reservation> findByIdAndPartnerEmailWithTicketAndSchedule(Long id, String email);

    Optional<Reservation> findByIdAndUserEmailWithSchedule(Long id, String email);

    Optional<Reservation> findByIdAndPartnerEmailWithTicketAndScheduleWithoutUnpaid(Long id, String email);

    List<Reservation> findAllByTicketIdWithTicketAndSchedule(Long ticketId);

    List<Reservation> findAllByPartnerEmailWithoutUnpaid(String email, Pageable pageable);
}
