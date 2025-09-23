package jiyeon.travel.domain.blog.dto;

import jiyeon.travel.domain.blog.entity.Blog;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class BlogSimpleResDto {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDate travelStartDate;
    private final LocalDate travelEndDate;
    private final int estimatedExpense;
    private final int totalExpense;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public BlogSimpleResDto(Blog blog) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.content = blog.getContent();
        this.travelStartDate = blog.getTravelStartDate();
        this.travelEndDate = blog.getTravelEndDate();
        this.estimatedExpense = blog.getEstimatedExpense();
        this.totalExpense = blog.getTotalExpense();
        this.createdAt = blog.getCreatedAt();
        this.updatedAt = blog.getUpdatedAt();
    }
}
