package jiyeon.travel.domain.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jiyeon.travel.domain.blog.entity.Blog;
import jiyeon.travel.domain.blog.entity.BlogImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "블로그 상세 응답")
public class BlogDetailResDto {

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

    @Schema(description = "블로그 이미지 목록")
    private final List<BlogImageDto> images;

    @Schema(description = "블로그에 등록된 티켓 사용 내역 목록")
    private final List<BlogTicketItemDto> items;

    @Schema(description = "생성일자")
    private final LocalDateTime createdAt;

    @Schema(description = "수정일자")
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
