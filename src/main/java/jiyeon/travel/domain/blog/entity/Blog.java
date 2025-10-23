package jiyeon.travel.domain.blog.entity;

import jakarta.persistence.*;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "blog")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Blog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDate travelStartDate;

    @Column(nullable = false)
    private LocalDate travelEndDate;

    @Column(nullable = false)
    private int estimatedExpense;

    @Column(nullable = false)
    private int totalExpense;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlogImage> blogImages = new ArrayList<>();

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlogTicketItem> blogTicketItems = new ArrayList<>();

    public Blog(User user, String title, String content, LocalDate travelStartDate, LocalDate travelEndDate, int estimatedExpense, int totalExpense) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.travelStartDate = travelStartDate;
        this.travelEndDate = travelEndDate;
        this.estimatedExpense = estimatedExpense;
        this.totalExpense = totalExpense;
    }

    public void changeTitle(String title) {
        if (title.isBlank()) {
            throw new IllegalArgumentException("제목이 비어있습니다.");
        }

        this.title = title;
    }

    public void changeContent(String content) {
        if (content.isBlank()) {
            throw new IllegalArgumentException("내용이 비어있습니다.");
        }

        this.content = content;
    }

    public void changeTravelStartDate(LocalDate travelStartDate) {
        this.travelStartDate = travelStartDate;
    }

    public void changeTravelEndDate(LocalDate travelEndDate) {
        this.travelEndDate = travelEndDate;
    }

    public void changeEstimatedExpense(int estimatedExpense) {
        this.estimatedExpense = estimatedExpense;
    }

    public void changeTotalExpense(int totalExpense) {
        this.totalExpense = totalExpense;
    }
}
