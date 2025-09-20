package jiyeon.travel.domain.blog.dto;

import jiyeon.travel.domain.blog.entity.BlogImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BlogImageDto {

    private final Long imageId;
    private final String imageUrl;
    private final String imageKey;
    private final String fileName;
    private final boolean isMain;

    public static BlogImageDto from(BlogImage blogImage) {
        return new BlogImageDto(
                blogImage.getId(),
                blogImage.getImageUrl(),
                blogImage.getImageKey(),
                blogImage.getFileName(),
                blogImage.isMain()
        );
    }
}
