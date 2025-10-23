package jiyeon.travel.domain.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jiyeon.travel.domain.blog.entity.Blog;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "티켓 사용 내역 목록 응답")
public class BlogTicketItemDetailsResDto {

    @Schema(description = "블로그 고유 식별자", example = "1")
    private final Long blogId;

    @Schema(description = "제목")
    private final String title;

    @Schema(description = "티켓 사용 내역 목록")
    private final List<BlogTicketItemDto> items;

    public BlogTicketItemDetailsResDto(Blog blog, List<BlogTicketItemDto> items) {
        this.blogId = blog.getId();
        this.title = blog.getTitle();
        this.items = items;
    }
}
