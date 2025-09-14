package jiyeon.travel.domain.reservation.entity;

import jakarta.persistence.*;
import jiyeon.travel.domain.blog.entity.BlogTicketItem;
import jiyeon.travel.domain.payment.entity.Payment;
import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.global.common.entity.BaseEntity;
import jiyeon.travel.global.common.enums.ReservationStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "reservation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_schedule_id", nullable = false)
    private TicketSchedule ticketSchedule;

    @Column(unique = true, length = 20, nullable = false)
    private String reservationNumber;

    @Column(nullable = false)
    private int totalQuantity;

    @Column(nullable = false)
    private int totalAmount;

    @Column(length = 50, nullable = false)
    private String reservationName;

    @Column(length = 20, nullable = false)
    private String reservationPhone;

    @Column(length = 30, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ReservationStatus status;

    private LocalDateTime cancelledAt;

    @OneToMany(mappedBy = "reservation")
    private List<ReservationOption> reservationOptions = new ArrayList<>();

    @OneToMany(mappedBy = "reservation")
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "reservation")
    private List<BlogTicketItem> blogTicketItems = new ArrayList<>();

    public Reservation(User user, TicketSchedule ticketSchedule, int totalQuantity, int totalAmount, String reservationName, String reservationPhone) {
        this.user = user;
        this.ticketSchedule = ticketSchedule;
        this.reservationNumber = createReservationNumber();
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.reservationName = reservationName;
        this.reservationPhone = reservationPhone;
        this.status = ReservationStatus.UNPAID;
    }

    public boolean isPaidStatus() {
        return this.status == ReservationStatus.PAID;
    }

    public void changeStatus(ReservationStatus status) {
        this.status = status;
    }

    private String createReservationNumber() {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        return now + "-" + uuid;
    }
}
