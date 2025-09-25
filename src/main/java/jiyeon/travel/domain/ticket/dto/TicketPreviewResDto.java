package jiyeon.travel.domain.ticket.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class TicketPreviewResDto {

    private final Long id;
    private final String ticketName;
    private final String saleStatus;
    private final String imageUrl;
    private final String fileName;
    private final boolean isMain;

    @QueryProjection
    public TicketPreviewResDto(Long id, String ticketName, String saleStatus, String imageUrl, String fileName, boolean isMain) {
        this.id = id;
        this.ticketName = ticketName;
        this.saleStatus = saleStatus;
        this.imageUrl = imageUrl;
        this.fileName = fileName;
        this.isMain = isMain;
    }
}
