package jiyeon.travel.domain.ticket.dto;

import jiyeon.travel.domain.ticket.entity.TicketImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TicketImageDto {

    private final Long imageId;
    private final String imageUrl;
    private final String imageKey;
    private final String fileName;
    private final boolean isMain;

    public static TicketImageDto from(TicketImage ticketImage) {
        return new TicketImageDto(
                ticketImage.getId(),
                ticketImage.getImageUrl(),
                ticketImage.getImageKey(),
                ticketImage.getFileName(),
                ticketImage.isMain()
        );
    }
}
