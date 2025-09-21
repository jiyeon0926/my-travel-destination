package jiyeon.travel.domain.blog.dto;

import jiyeon.travel.domain.blog.entity.Blog;
import jiyeon.travel.domain.blog.entity.BlogImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BlogImageDetailsResDto {

    private final Long blogId;
    private final String title;
    private final List<BlogImageDto> images;

    public BlogImageDetailsResDto(Blog blog, List<BlogImage> images) {
        this.blogId = blog.getId();
        this.title = blog.getTitle();
        this.images = images.stream().map(BlogImageDto::from).toList();
    }
}
