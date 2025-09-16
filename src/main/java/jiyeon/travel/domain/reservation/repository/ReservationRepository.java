package jiyeon.travel.domain.reservation.repository;

import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.global.common.enums.ReservationStatus;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, CustomReservationRepository {

    default Reservation findByIdOrElseThrow(Long id) {
        return this.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    default Reservation findByIdAndEmailOrElseThrow(Long id, String email) {
        return this.findByIdAndEmail(id, email)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    @Query("select r from Reservation r inner join fetch r.user u where r.id = :id and u.email = :email")
    Optional<Reservation> findByIdAndEmail(Long id, String email);

    @Query("select r from Reservation r inner join fetch r.ticketSchedule s where r.id = :id")
    Optional<Reservation> findByIdWithSchedule(Long id);

    @Query("select r from Reservation r inner join fetch r.user u where u.email = :email and r.status = :status")
    List<Reservation> findAllByEmailAndStatus(String email, ReservationStatus status);
}
