package jiyeon.travel.domain.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jiyeon.travel.domain.blog.entity.Blog;
import jiyeon.travel.domain.blog.entity.BlogImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Schema(description = "블로그 이미지 상세 응답")
public class BlogImageDetailResDto {

    @Schema(description = "블로그 고유 식별자", example = "1")
    private final Long blogId;

    @Schema(description = "블로그 제목")
    private final String title;

    @Schema(description = "이미지 고유 식별자", example = "1")
    private final Long imageId;

    @Schema(description = "AWS S3 이미지 URL", example = "https://bucket.s3.region.amazonaws.com/blog/yyyyMMdd/UUID_파일명")
    private final String imageUrl;

    @Schema(description = "파일명", example = "image.jpg")
    private final String fileName;

    @Schema(description = "대표 이미지 여부", example = "true")
    private final Boolean isMain;

    @Schema(description = "생성일자")
    private final LocalDateTime createdAt;

    @Schema(description = "수정일자")
    private final LocalDateTime updatedAt;

    public BlogImageDetailResDto(Blog blog, BlogImage blogImage) {
        this.blogId = blog.getId();
        this.title = blog.getTitle();
        this.imageId = blogImage.getId();
        this.imageUrl = blogImage.getImageUrl();
        this.fileName = blogImage.getFileName();
        this.isMain = blogImage.isMain();
        this.createdAt = blogImage.getCreatedAt();
        this.updatedAt = blogImage.getUpdatedAt();
    }
}
