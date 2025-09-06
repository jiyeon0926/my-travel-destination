package jiyeon.travel.domain.ticket.entity;

import jakarta.persistence.*;
import jiyeon.travel.domain.reservation.entity.ReservationOption;
import jiyeon.travel.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ticket_option")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TicketOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @OneToMany(mappedBy = "ticketOption")
    private List<ReservationOption> reservationOptions = new ArrayList<>();

    public TicketOption(Ticket ticket, String name, int price) {
        this.ticket = ticket;
        this.name = name;
        this.price = price;
    }

    public void changeName(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("이름이 비어있습니다.");
        }

        this.name = name;
    }

    public void changePrice(int price) {
        this.price = price;
    }
}
