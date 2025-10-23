package jiyeon.travel.domain.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jiyeon.travel.domain.blog.entity.Blog;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Schema(description = "블로그 기본 응답")
public class BlogSimpleResDto {

    @Schema(description = "블로그 고유 식별자", example = "1")
    private final Long id;

    @Schema(description = "제목")
    private final String title;

    @Schema(description = "내용")
    private final String content;

    @Schema(description = "여행 시작일", example = "2025-09-12")
    private final LocalDate travelStartDate;

    @Schema(description = "여행 종료일", example = "2025-09-15")
    private final LocalDate travelEndDate;

    @Schema(description = "예상 경비", example = "500000")
    private final int estimatedExpense;

    @Schema(description = "총 경비", example = "540000")
    private final int totalExpense;

    @Schema(description = "생성일자")
    private final LocalDateTime createdAt;

    @Schema(description = "수정일자")
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
