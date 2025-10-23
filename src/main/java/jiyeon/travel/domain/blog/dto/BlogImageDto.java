package jiyeon.travel.domain.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jiyeon.travel.domain.blog.entity.BlogImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "블로그 이미지 정보")
public class BlogImageDto {

    @Schema(description = "블로그 이미지 고유 식별자", example = "1")
    private final Long imageId;

    @Schema(description = "AWS S3 이미지 URL", example = "https://bucket.s3.region.amazonaws.com/blog/yyyyMMdd/UUID_파일명")
    private final String imageUrl;

    @Schema(description = "파일명", example = "image.jpg")
    private final String fileName;

    @Schema(description = "대표 이미지 여부", example = "true")
    private final Boolean isMain;

    public static BlogImageDto from(BlogImage blogImage) {
        return new BlogImageDto(
                blogImage.getId(),
                blogImage.getImageUrl(),
                blogImage.getImageKey(),
                blogImage.isMain()
        );
    }
}
