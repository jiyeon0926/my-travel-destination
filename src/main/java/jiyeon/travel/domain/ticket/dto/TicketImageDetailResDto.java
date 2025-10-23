package jiyeon.travel.domain.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Schema(description = "티켓 이미지 상세 응답")
public class TicketImageDetailResDto {

    @Schema(description = "티켓 고유 식별자", example = "1")
    private final Long ticketId;

    @Schema(description = "티켓명")
    private final String ticketName;

    @Schema(description = "티켓 이미지 고유 식별자", example = "1")
    private final Long imageId;

    @Schema(description = "AWS S3 이미지 URL", example = "https://bucket.s3.region.amazonaws.com/ticket/yyyyMMdd/UUID_파일명")
    private final String imageUrl;

    @Schema(description = "파일명", example = "image.jpg")
    private final String fileName;

    @Schema(description = "대표 이미지 여부", example = "true")
    private final Boolean isMain;

    @Schema(description = "생성일자")
    private final LocalDateTime createdAt;

    @Schema(description = "수정일자")
    private final LocalDateTime updatedAt;

    public TicketImageDetailResDto(Ticket ticket, TicketImage ticketImage) {
        this.ticketId = ticket.getId();
        this.ticketName = ticket.getName();
        this.imageId = ticketImage.getId();
        this.imageUrl = ticketImage.getImageUrl();
        this.fileName = ticketImage.getFileName();
        this.isMain = ticketImage.isMain();
        this.createdAt = ticketImage.getCreatedAt();
        this.updatedAt = ticketImage.getUpdatedAt();
    }
}
