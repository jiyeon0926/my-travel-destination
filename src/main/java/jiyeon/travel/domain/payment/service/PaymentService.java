package jiyeon.travel.domain.payment.service;

import jiyeon.travel.domain.payment.dto.KakaopayCompletedResDto;
import jiyeon.travel.domain.payment.dto.KakaopayReadyResDto;
import jiyeon.travel.domain.payment.entity.Payment;
import jiyeon.travel.domain.payment.repository.PaymentRepository;
import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.domain.reservation.repository.ReservationRepository;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.repository.TicketRepository;
import jiyeon.travel.global.common.enums.ReservationStatus;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final KakaopayService kakaopayService;
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public KakaopayReadyResDto readyPayment(String email, Long reservationId) {
        Reservation reservation = reservationRepository.findByIdAndEmailOrElseThrow(reservationId, email);
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
        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationId);

        KakaopayCompletedResDto kakaopayCompletedResDto = kakaopayService.approvePayment(payment, reservation, pgToken);

        payment.completedPayment(kakaopayCompletedResDto.getPaymentMethod(), kakaopayCompletedResDto.getApprovedAt());
        reservation.changeStatus(ReservationStatus.PAID);

        return kakaopayCompletedResDto;
    }
}
