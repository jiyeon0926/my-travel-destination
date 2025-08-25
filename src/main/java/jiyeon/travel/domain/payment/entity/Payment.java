package jiyeon.travel.domain.payment.entity;

import jakarta.persistence.*;
import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.global.common.entity.BaseEntity;
import jiyeon.travel.global.common.enums.PaymentStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(nullable = false)
    private int amount;

    @Column(length = 50, nullable = false)
    private String paymentMethod;

    @Column(length = 50, nullable = false)
    private String paymentGateway;

    @Column(length = 100, nullable = false)
    private String transactionId;

    @Column(length = 30, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime paidAt;

    @Builder
    public Payment(Reservation reservation, int amount, String paymentMethod, String paymentGateway, String transactionId, LocalDateTime paidAt) {
        this.reservation = reservation;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentGateway = paymentGateway;
        this.transactionId = transactionId;
        this.status = PaymentStatus.READY;
        this.paidAt = paidAt;
    }
}
