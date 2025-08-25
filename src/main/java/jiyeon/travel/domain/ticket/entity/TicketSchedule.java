package jiyeon.travel.domain.ticket.entity;

import jakarta.persistence.*;
import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ticket_schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TicketSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalTime startTime;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int remainingQuantity;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations = new ArrayList<>();

    public TicketSchedule(Ticket ticket, LocalDate startDate, LocalTime startTime, int quantity) {
        this.ticket = ticket;
        this.isActive = true;
        this.startDate = startDate;
        this.startTime = startTime;
        this.quantity = quantity;
        this.remainingQuantity = quantity;
    }
}
