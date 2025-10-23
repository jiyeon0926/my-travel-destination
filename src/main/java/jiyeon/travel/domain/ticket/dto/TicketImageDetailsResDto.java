package jiyeon.travel.domain.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "티켓 이미지 목록 응답")
public class TicketImageDetailsResDto {

    @Schema(description = "티켓 고유 식별자", example = "1")
    private final Long ticketId;

    @Schema(description = "티켓명")
    private final String ticketName;

    @Schema(description = "티켓 이미지 목록")
    private final List<TicketImageDto> images;

    public TicketImageDetailsResDto(Ticket ticket, List<TicketImage> images) {
        this.ticketId = ticket.getId();
        this.ticketName = ticket.getName();
        this.images = images.stream().map(TicketImageDto::from).toList();
    }
}
