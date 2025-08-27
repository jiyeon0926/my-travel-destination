package jiyeon.travel.domain.partner.entity;

import jakarta.persistence.*;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "partner")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Partner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(unique = true, nullable = false)
    private String businessNumber;

    @Column(nullable = false)
    private String address;

    public Partner(User user, String businessNumber, String address) {
        this.user = user;
        this.businessNumber = businessNumber;
        this.address = address;
    }
}
