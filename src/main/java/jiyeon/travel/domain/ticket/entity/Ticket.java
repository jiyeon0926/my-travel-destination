package jiyeon.travel.domain.ticket.entity;

import jakarta.persistence.*;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.global.common.entity.BaseEntity;
import jiyeon.travel.global.common.enums.TicketStatus;
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
    private TicketStatus status;

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
        this.status = TicketStatus.READY;
    }

    public void updateTicketInfo(String name, LocalDateTime saleStartDate, LocalDateTime saleEndDate, Integer basePrice, String phone, String address, String description) {
        if (name != null) changeName(name);
        if (saleStartDate != null) this.saleStartDate = saleStartDate;
        if (saleEndDate != null) this.saleEndDate = saleEndDate;
        if (basePrice != null) this.basePrice = basePrice;
        if (phone != null) changePhone(phone);
        if (address != null) changeAddress(address);
        if (description != null) this.description = description;
    }

    private void changeName(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("이름이 비어있습니다.");
        }

        this.name = name;
    }

    private void changePhone(String phone) {
        if (phone.isBlank()) {
            throw new IllegalArgumentException("전화번호가 비어있습니다.");
        }

        this.phone = phone;
    }

    private void changeAddress(String address) {
        if (address.isBlank()) {
            throw new IllegalArgumentException("주소가 비어있습니다.");
        }

        this.address = address;
    }
}
