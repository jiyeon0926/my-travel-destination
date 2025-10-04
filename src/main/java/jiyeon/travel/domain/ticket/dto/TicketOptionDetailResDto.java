package jiyeon.travel.domain.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Schema(description = "티켓 옵션 상세 응답")
public class TicketOptionDetailResDto {

    @Schema(description = "티켓 고유 식별자", example = "1")
    private final Long ticketId;

    @Schema(description = "티켓명")
    private final String ticketName;

    @Schema(description = "티켓 옵션 고유 식별자", example = "1")
    private final Long optionId;

    @Schema(description = "옵션명", example = "청소년 이용권")
    private final String name;

    @Schema(description = "옵션의 가격", example = "8000")
    private final int price;

    @Schema(description = "생성일자")
    private final LocalDateTime createdAt;

    @Schema(description = "수정일자")
    private final LocalDateTime updatedAt;

    public TicketOptionDetailResDto(Ticket ticket, TicketOption ticketOption) {
        this.ticketId = ticket.getId();
        this.ticketName = ticket.getName();
        this.optionId = ticketOption.getId();
        this.name = ticketOption.getName();
        this.price = ticketOption.getPrice();
        this.createdAt = ticketOption.getCreatedAt();
        this.updatedAt = ticketOption.getUpdatedAt();
    }
}
