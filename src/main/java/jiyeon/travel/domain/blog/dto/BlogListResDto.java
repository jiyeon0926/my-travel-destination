package jiyeon.travel.domain.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "블로그 목록 응답")
public class BlogListResDto {

    @Schema(description = "합계", example = "10")
    private final Long total;

    @Schema(description = "블로그 목록")
    private final List<BlogPreviewResDto> blogs;
}
