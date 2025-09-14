package jiyeon.travel.domain.payment.service;

import jiyeon.travel.domain.payment.dto.KakaopayCompletedResDto;
import jiyeon.travel.domain.payment.dto.KakaopayReadyResDto;
import jiyeon.travel.domain.payment.entity.Payment;
import jiyeon.travel.domain.payment.repository.PaymentRepository;
import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.domain.reservation.service.ReservationService;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.repository.TicketRepository;
import jiyeon.travel.global.common.enums.PaymentStatus;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final KakaopayService kakaopayService;
    private final ReservationService reservationService;
    private final TicketRepository ticketRepository;

    @Transactional
    public KakaopayReadyResDto readyPayment(Long reservationId) {
        Reservation reservation = reservationService.getReservationById(reservationId);
        if (reservation.isPaidStatus()) {
            throw new CustomException(ErrorCode.ALREADY_PAID_RESERVATION);
        }

        Optional<Payment> readyPayment = paymentRepository.findByReservationIdAndStatus(reservationId, PaymentStatus.READY);
        readyPayment.ifPresent(paymentRepository::delete);

        Ticket ticket = ticketRepository.getTicketByReservationId(reservationId);

        KakaopayReadyResDto kakaopayReadyResDto = kakaopayService.readyPayment(reservation, ticket);

        Payment payment = new Payment(
                reservation,
                reservation.getTotalAmount(),
                reservation.getTotalQuantity(),
                kakaopayReadyResDto.getTid()
        );
        paymentRepository.save(payment);

        return kakaopayReadyResDto;
    }

    @Transactional
    public KakaopayCompletedResDto completedPayment(Long reservationId, String pgToken) {
        Payment payment = paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

        KakaopayCompletedResDto kakaopayCompletedResDto = kakaopayService.approvePayment(payment.getTid(), payment.getReservation(), pgToken);

        payment.completedPayment(kakaopayCompletedResDto.getPaymentMethod(), kakaopayCompletedResDto.getApprovedAt());
        reservationService.paidReservation(reservationId);

        return kakaopayCompletedResDto;
    }
}
