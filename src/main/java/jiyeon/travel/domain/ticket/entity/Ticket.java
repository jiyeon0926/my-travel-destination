package jiyeon.travel.domain.ticket.entity;

import jakarta.persistence.*;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.global.common.entity.BaseEntity;
import jiyeon.travel.global.common.enums.TicketSaleStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ticket")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Ticket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime saleStartDate;

    @Column(nullable = false)
    private LocalDateTime saleEndDate;

    private Integer basePrice;

    @Column(length = 20, nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(columnDefinition = "text")
    private String description;

    @Column(length = 30, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TicketSaleStatus saleStatus;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketOption> ticketOptions = new ArrayList<>();

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketSchedule> ticketSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketImage> ticketImages = new ArrayList<>();

    @Builder
    public Ticket(User user, String name, LocalDateTime saleStartDate, LocalDateTime saleEndDate, Integer basePrice, String phone, String address, String description) {
        this.user = user;
        this.name = name;
        this.saleStartDate = saleStartDate;
        this.saleEndDate = saleEndDate;
        this.basePrice = basePrice;
        this.phone = phone;
        this.address = address;
        this.description = description;
        this.saleStatus = TicketSaleStatus.READY;
    }

    public boolean isReadyStatus() {
        return saleStatus == TicketSaleStatus.READY;
    }

    public boolean isNotReadyStatus() {
        return !isReadyStatus();
    }

    public boolean isActiveStatus() {
        return saleStatus == TicketSaleStatus.ACTIVE;
    }

    public boolean isNotActiveStatus() {
        return !isActiveStatus();
    }

    public boolean isSoldOutStatus() {
        return saleStatus == TicketSaleStatus.SOLD_OUT;
    }

    public boolean isClosedStatus() {
        return saleStatus == TicketSaleStatus.CLOSED;
    }

    public boolean canUpdateImage() {
        return isReadyStatus() || isActiveStatus();
    }

    public boolean cannotUpdateImage() {
        return !canUpdateImage();
    }

    public boolean hasSaleStarted(LocalDateTime referenceTime) {
        return !this.saleStartDate.isAfter(referenceTime);
    }

    public boolean hasSaleEnded(LocalDateTime referenceTime) {
        return !this.saleEndDate.isAfter(referenceTime);
    }

    public void changeName(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("이름이 비어있습니다.");
        }

        this.name = name;
    }

    public void changeSaleStartDate(LocalDateTime saleStartDate) {
        this.saleStartDate = saleStartDate;
    }

    public void changeSaleEndDate(LocalDateTime saleEndDate) {
        this.saleEndDate = saleEndDate;
    }

    public void changeBasePrice(Integer basePrice) {
        this.basePrice = basePrice;
    }

    public void changePhone(String phone) {
        if (phone.isBlank()) {
            throw new IllegalArgumentException("전화번호가 비어있습니다.");
        }

        this.phone = phone;
    }

    public void changeAddress(String address) {
        if (address.isBlank()) {
            throw new IllegalArgumentException("주소가 비어있습니다.");
        }

        this.address = address;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeSaleStatus(TicketSaleStatus saleStatus) {
        this.saleStatus = saleStatus;
    }
}
