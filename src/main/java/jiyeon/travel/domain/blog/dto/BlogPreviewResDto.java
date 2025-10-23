package jiyeon.travel.domain.blog.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "블로그 미리보기 응답")
public class BlogPreviewResDto {

    @Schema(description = "블로그 고유 식별자", example = "1")
    private final Long id;

    @Schema(description = "제목")
    private final String title;

    @Schema(description = "AWS S3 이미지 URL", example = "https://bucket.s3.region.amazonaws.com/blog/yyyyMMdd/UUID_파일명")
    private final String imageUrl;

    @Schema(description = "파일명", example = "image.jpg")
    private final String fileName;

    @Schema(description = "대표 이미지 여부", example = "true")
    private final Boolean isMain;

    @QueryProjection
    public BlogPreviewResDto(Long id, String title, String imageUrl, String fileName, boolean isMain) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.fileName = fileName;
        this.isMain = isMain;
    }
}
