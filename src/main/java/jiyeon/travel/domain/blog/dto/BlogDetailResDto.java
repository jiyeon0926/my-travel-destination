package jiyeon.travel.domain.blog.dto;

import jiyeon.travel.domain.blog.entity.Blog;
import jiyeon.travel.domain.blog.entity.BlogImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class BlogDetailResDto {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDate travelStartDate;
    private final LocalDate travelEndDate;
    private final int estimatedExpense;
    private final int totalExpense;
    private final List<BlogImageDto> images;
    private final List<BlogTicketItemDto> items;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public BlogDetailResDto(Blog blog, List<BlogImage> images, List<BlogTicketItemDto> items) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.content = blog.getContent();
        this.travelStartDate = blog.getTravelStartDate();
        this.travelEndDate = blog.getTravelEndDate();
        this.estimatedExpense = blog.getEstimatedExpense();
        this.totalExpense = blog.getTotalExpense();
        this.images = images.stream().map(BlogImageDto::from).toList();
        this.items = items;
        this.createdAt = blog.getCreatedAt();
        this.updatedAt = blog.getUpdatedAt();
    }
}
