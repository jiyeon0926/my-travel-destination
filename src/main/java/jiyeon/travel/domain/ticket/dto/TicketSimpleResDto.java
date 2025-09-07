package jiyeon.travel.domain.ticket.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TicketSimpleResDto {

    private final Long id;
    private final String ticketName;
    private final LocalDateTime saleStartDate;
    private final LocalDateTime saleEndDate;
    private final String saleStatus;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @QueryProjection
    public TicketSimpleResDto(Long id, String ticketName, LocalDateTime saleStartDate, LocalDateTime saleEndDate, String saleStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.ticketName = ticketName;
        this.saleStartDate = saleStartDate;
        this.saleEndDate = saleEndDate;
        this.saleStatus = saleStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
