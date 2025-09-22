package jiyeon.travel.domain.blog.dto;

import jiyeon.travel.domain.blog.entity.Blog;
import jiyeon.travel.domain.blog.entity.BlogImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class BlogImageDetailResDto {

    private final Long blogId;
    private final String title;
    private final Long imageId;
    private final String imageUrl;
    private final String imageKey;
    private final String fileName;
    private final boolean isMain;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public BlogImageDetailResDto(Blog blog, BlogImage blogImage) {
        this.blogId = blog.getId();
        this.title = blog.getTitle();
        this.imageId = blogImage.getId();
        this.imageUrl = blogImage.getImageUrl();
        this.imageKey = blogImage.getImageKey();
        this.fileName = blogImage.getFileName();
        this.isMain = blogImage.isMain();
        this.createdAt = blogImage.getCreatedAt();
        this.updatedAt = blogImage.getUpdatedAt();
    }
}
