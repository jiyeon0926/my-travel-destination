package jiyeon.travel.domain.blog.entity;

import jakarta.persistence.*;
import jiyeon.travel.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "blog_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BlogImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;

    @Column(length = 500, nullable = false)
    private String imageUrl;

    @Column(length = 500, nullable = false)
    private String imageKey;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private boolean isMain;

    public BlogImage(Blog blog, String imageUrl, String imageKey, String fileName, boolean isMain) {
        this.blog = blog;
        this.imageUrl = imageUrl;
        this.imageKey = imageKey;
        this.fileName = fileName;
        this.isMain = isMain;
    }

    public void changeImageMain(boolean isMain) {
        this.isMain = isMain;
    }
}
