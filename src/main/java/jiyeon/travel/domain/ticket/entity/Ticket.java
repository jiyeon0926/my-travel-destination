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
        this.saleStartDate = validateSaleStartDate(saleStartDate, LocalDateTime.now());
        this.saleEndDate = validateSaleEndDate(saleEndDate);
        this.basePrice = basePrice;
        this.phone = phone;
        this.address = address;
        this.description = description;
        this.saleStatus = TicketSaleStatus.READY;
    }

    public boolean isReadyStatus() {
        return saleStatus == (TicketSaleStatus.READY);
    }

    public void changeName(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("이름이 비어있습니다.");
        }

        this.name = name;
    }

    public void changeSaleStartDate(LocalDateTime saleStartDate, LocalDateTime now) {
        this.saleStartDate = validateSaleStartDate(saleStartDate, now);
    }

    public void changeSaleEndDate(LocalDateTime saleEndDate) {
        this.saleEndDate = validateSaleEndDate(saleEndDate);
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

    private LocalDateTime validateSaleStartDate(LocalDateTime saleStartDate, LocalDateTime now) {
        if (saleStartDate.isBefore(now)) {
            throw new IllegalArgumentException("판매 시작일은 현재 시간 이후여야 합니다.");
        }

        return saleStartDate;
    }

    private LocalDateTime validateSaleEndDate(LocalDateTime saleEndDate) {
        if (saleEndDate.isBefore(this.saleStartDate)) {
            throw new IllegalArgumentException("판매 종료일은 시작일 이후여야 합니다.");
        }

        return saleEndDate;
    }
}
