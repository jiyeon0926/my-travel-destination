package jiyeon.travel.domain.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jiyeon.travel.domain.blog.entity.Blog;
import jiyeon.travel.domain.blog.entity.BlogImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "블로그 이미지 목록 응답")
public class BlogImageDetailsResDto {

    @Schema(description = "블로그 고유 식별자", example = "1")
    private final Long blogId;

    @Schema(description = "블로그 제목")
    private final String title;

    @Schema(description = "블로그 이미지 정보 목록")
    private final List<BlogImageDto> images;

    public BlogImageDetailsResDto(Blog blog, List<BlogImage> images) {
        this.blogId = blog.getId();
        this.title = blog.getTitle();
        this.images = images.stream().map(BlogImageDto::from).toList();
    }
}
