package jiyeon.travel.domain.ticket.entity;

import jakarta.persistence.*;
import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.global.common.entity.BaseEntity;
import jiyeon.travel.global.common.enums.TicketSaleStatus;
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

    @OneToMany(mappedBy = "ticketSchedule")
    private List<Reservation> reservations = new ArrayList<>();

    public TicketSchedule(Ticket ticket, LocalDate startDate, LocalTime startTime, int quantity) {
        this.ticket = ticket;
        this.isActive = true;
        this.startDate = startDate;
        this.startTime = startTime;
        this.quantity = quantity;
        this.remainingQuantity = quantity;
    }

    public boolean isReadyStatus() {
        return hasSaleStatus(TicketSaleStatus.READY);
    }

    public boolean isNotReadyStatus() {
        return !isReadyStatus();
    }

    public boolean isSoldOut() {
        return this.remainingQuantity == 0;
    }

    public void changeIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void changeStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void changeStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void increaseQuantity(Integer newQuantity) {
        if (newQuantity < this.quantity) {
            throw new IllegalArgumentException("수량은 기존보다 줄일 수 없습니다.");
        }

        this.remainingQuantity += (newQuantity - this.quantity);
        this.quantity = newQuantity;
    }

    public void decreaseRemainingQuantity(int totalQuantity) {
        if (this.remainingQuantity < totalQuantity) {
            throw new IllegalArgumentException("남은 수량보다 큰 수량을 감소시킬 수 없습니다.");
        }

        this.remainingQuantity -= totalQuantity;
    }

    public void increaseRemainingQuantity(int quantity) {
        this.remainingQuantity += quantity;
    }

    private boolean hasSaleStatus(TicketSaleStatus status) {
        return ticket.getSaleStatus() == status;
    }
}
