package jiyeon.travel.domain.reservation.entity;

import jakarta.persistence.*;
import jiyeon.travel.domain.ticket.entity.TicketOption;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation_option")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class ReservationOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_option_id", nullable = false)
    private TicketOption ticketOption;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int unitPrice;

    @Column(nullable = false)
    private int totalPrice;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    public ReservationOption(Reservation reservation, TicketOption ticketOption, int quantity, int unitPrice) {
        this.reservation = reservation;
        this.ticketOption = ticketOption;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = calculateTotalPrice();
    }

    private int calculateTotalPrice() {
        return this.quantity * this.unitPrice;
    }
}
