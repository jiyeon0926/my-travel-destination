package jiyeon.travel.domain.reservation.repository;

import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.global.common.enums.ReservationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, CustomReservationRepository {

    @Query("select r from Reservation r inner join fetch r.user u where r.id = :id and u.email = :email and r.status = :status")
    Optional<Reservation> findByIdAndEmailAndStatus(Long id, String email, ReservationStatus status);

    @Query("select r from Reservation r inner join fetch r.user u where u.email = :email and r.status = :status")
    List<Reservation> findAllByEmailAndStatus(String email, ReservationStatus status);

    @Query("select r from Reservation r inner join fetch r.user u where u.email = :email")
    List<Reservation> findAllByEmail(String email, Pageable pageable);

    @Query("select r from Reservation r inner join fetch r.ticketSchedule s where r.status = :status")
    List<Reservation> findAllByStatusWithSchedule(ReservationStatus status);
}
