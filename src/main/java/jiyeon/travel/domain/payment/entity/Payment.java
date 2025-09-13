package jiyeon.travel.domain.payment.entity;

import jakarta.persistence.*;
import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.global.common.entity.BaseEntity;
import jiyeon.travel.global.common.enums.PaymentStatus;
import lombok.AccessLevel;
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

    @Column(nullable = false)
    private int quantity;

    @Column(length = 50)
    private String paymentMethod;

    @Column(length = 100, nullable = false)
    private String tid;

    @Column(length = 30, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime approvedAt;

    public Payment(Reservation reservation, int amount, int quantity, String tid) {
        this.reservation = reservation;
        this.amount = amount;
        this.quantity = quantity;
        this.tid = tid;
        this.status = PaymentStatus.READY;
    }

    public void completedPayment(String paymentMethod, LocalDateTime approvedAt) {
        this.paymentMethod = paymentMethod;
        this.approvedAt = approvedAt;
        this.status = PaymentStatus.COMPLETED;
    }
}
