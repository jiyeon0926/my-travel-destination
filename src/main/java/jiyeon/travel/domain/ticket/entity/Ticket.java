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

    @OneToMany(mappedBy = "ticket")
    private List<TicketOption> ticketOptions = new ArrayList<>();

    @OneToMany(mappedBy = "ticket")
    private List<TicketSchedule> ticketSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "ticket")
    private List<TicketImage> ticketImages = new ArrayList<>();

    @Builder
    public Ticket(User user, LocalDateTime saleStartDate, LocalDateTime saleEndDate, Integer basePrice, String phone, String address, String description) {
        this.user = user;
        this.saleStartDate = saleStartDate;
        this.saleEndDate = saleEndDate;
        this.basePrice = basePrice;
        this.phone = phone;
        this.address = address;
        this.description = description;
        this.status = TicketStatus.READY;
    }
}
