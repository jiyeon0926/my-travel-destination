package jiyeon.travel.domain.ticket.dto;

import jiyeon.travel.domain.ticket.entity.TicketImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class TicketImageDetailResDto {

    private final Long ticketId;
    private final String ticketName;
    private final Long imageId;
    private final String imageUrl;
    private final String imageKey;
    private final String fileName;
    private final boolean isMain;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public TicketImageDetailResDto(TicketImage ticketImage) {
        this.ticketId = ticketImage.getTicket().getId();
        this.ticketName = ticketImage.getTicket().getName();
        this.imageId = ticketImage.getId();
        this.imageUrl = ticketImage.getImageUrl();
        this.imageKey = ticketImage.getImageKey();
        this.fileName = ticketImage.getFileName();
        this.isMain = ticketImage.isMain();
        this.createdAt = ticketImage.getCreatedAt();
        this.updatedAt = ticketImage.getUpdatedAt();
    }
}
