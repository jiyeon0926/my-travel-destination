package jiyeon.travel.domain.blog.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BlogListResDto {

    private final Long total;
    private final List<BlogPreviewResDto> blogs;
}
