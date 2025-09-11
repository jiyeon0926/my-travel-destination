package jiyeon.travel.domain.reservation.repository;

import jiyeon.travel.domain.reservation.entity.ReservationOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationOptionRepository extends JpaRepository<ReservationOption, Long> {
}
