package jiyeon.travel.domain.ticket.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(description = "티켓과 관련된 블로그 정보")
public class TicketBlogDto {

    @Schema(description = "블로그 고유 식별자", example = "1")
    private final Long blogId;

    @Schema(description = "제목")
    private final String title;

    @Schema(description = "생성일자")
    private final LocalDateTime createdAt;

    @QueryProjection
    public TicketBlogDto(Long blogId, String title, LocalDateTime createdAt) {
        this.blogId = blogId;
        this.title = title;
        this.createdAt = createdAt;
    }
}
