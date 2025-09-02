package jiyeon.travel.domain.ticket.dto;

import jiyeon.travel.domain.ticket.entity.TicketImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TicketImageResDto {

    private final Long imageId;
    private final String imageUrl;
    private final String fileName;
    private final boolean isMain;

    public static TicketImageResDto from(TicketImage ticketImage) {
        return new TicketImageResDto(
                ticketImage.getId(),
                ticketImage.getImageUrl(),
                ticketImage.getFileName(),
                ticketImage.isMain()
        );
    }
}
