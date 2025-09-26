package jiyeon.travel.domain.ticket.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TicketBlogDto {

    private final Long blogId;
    private final String title;
    private final LocalDateTime createdAt;

    @QueryProjection
    public TicketBlogDto(Long blogId, String title, LocalDateTime createdAt) {
        this.blogId = blogId;
        this.title = title;
        this.createdAt = createdAt;
    }
}
