package jiyeon.travel.domain.blog.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class BlogPreviewResDto {

    private final Long id;
    private final String title;
    private final String imageUrl;
    private final String fileName;
    private final boolean isMain;

    @QueryProjection
    public BlogPreviewResDto(Long id, String title, String imageUrl, String fileName, boolean isMain) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.fileName = fileName;
        this.isMain = isMain;
    }
}
