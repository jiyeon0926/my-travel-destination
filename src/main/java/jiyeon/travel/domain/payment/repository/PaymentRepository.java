package jiyeon.travel.domain.payment.repository;

import jiyeon.travel.domain.payment.entity.Payment;
import jiyeon.travel.global.common.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByReservationId(Long reservationId);

    Optional<Payment> getByReservationIdAndStatus(Long reservationId, PaymentStatus status);
}
