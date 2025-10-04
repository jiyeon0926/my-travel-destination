package jiyeon.travel.domain.ticket.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "티켓 미리보기 응답")
public class TicketPreviewResDto {

    @Schema(description = "티켓 고유 식별자", example = "1")
    private final Long id;

    @Schema(description = "티켓명")
    private final String ticketName;

    @Schema(description = "티켓 판매 상태", example = "ACTIVE")
    private final String saleStatus;

    @Schema(description = "AWS S3 이미지 URL", example = "https://bucket.s3.region.amazonaws.com/ticket/yyyyMMdd/UUID_파일명")
    private final String imageUrl;

    @Schema(description = "파일명", example = "image.jpg")
    private final String fileName;

    @Schema(description = "대표 이미지 여부", example = "true")
    private final Boolean isMain;

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
