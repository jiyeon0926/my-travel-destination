package jiyeon.travel.domain.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jiyeon.travel.domain.ticket.entity.TicketImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "티켓 이미지 정보")
public class TicketImageDto {

    @Schema(description = "티켓 이미지 고유 식별자", example = "1")
    private final Long imageId;

    @Schema(description = "AWS S3 이미지 URL", example = "https://bucket.s3.region.amazonaws.com/ticket/yyyyMMdd/UUID_파일명")
    private final String imageUrl;

    @Schema(description = "파일명", example = "image.jpg")
    private final String fileName;

    @Schema(description = "대표 이미지 여부", example = "true")
    private final Boolean isMain;

    public static TicketImageDto from(TicketImage ticketImage) {
        return new TicketImageDto(
                ticketImage.getId(),
                ticketImage.getImageUrl(),
                ticketImage.getFileName(),
                ticketImage.isMain()
        );
    }
}
