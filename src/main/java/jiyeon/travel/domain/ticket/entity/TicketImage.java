package jiyeon.travel.domain.ticket.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class TicketImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(length = 500, nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private boolean isMain;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    public TicketImage(Ticket ticket, String imageUrl, String fileName) {
        this.ticket = ticket;
        this.imageUrl = imageUrl;
        this.fileName = fileName;
    }
}
