package jiyeon.travel.domain.blog.dto;

import jiyeon.travel.domain.blog.entity.Blog;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BlogTicketItemDetailsResDto {

    private final Long blogId;
    private final String title;
    private final List<BlogTicketItemDto> items;

    public BlogTicketItemDetailsResDto(Blog blog, List<BlogTicketItemDto> items) {
        this.blogId = blog.getId();
        this.title = blog.getTitle();
        this.items = items;
    }
}
