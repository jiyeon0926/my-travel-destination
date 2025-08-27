package jiyeon.travel.domain.user.entity;

import jakarta.persistence.*;
import jiyeon.travel.domain.blog.entity.Blog;
import jiyeon.travel.domain.partner.entity.Partner;
import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.global.common.entity.BaseEntity;
import jiyeon.travel.global.common.enums.UserRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 200, nullable = false)
    private String password;

    @Column(length = 60, nullable = false)
    private String displayName;

    @Column(length = 20, nullable = false)
    private String phone;

    @Column(length = 30, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    private boolean isDeleted;

    @OneToMany(mappedBy = "user")
    private List<Partner> partners = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Ticket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Blog> blogs = new ArrayList<>();

    public User(String email, String password, String displayName, String phone, UserRole role) {
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.phone = phone;
        this.role = role;
    }

    public void changeIsDeleted() {
        this.isDeleted = true;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateProfile(String nickname, String phone) {
        if (nickname != null) {
            changeNickname(nickname);
        }

        if (phone != null) {
            changePhone(phone);
        }
    }

    private void changeNickname(String nickname) {
        if (nickname.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "닉네임이 비어있습니다.");
        }

        this.displayName = nickname;
    }

    private void changePhone(String phone) {
        if (phone.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "전화번호가 비어있습니다.");
        }

        this.phone = phone;
    }
}
