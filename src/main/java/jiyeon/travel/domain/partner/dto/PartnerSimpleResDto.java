package jiyeon.travel.domain.partner.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(description = "업체 기본 응답")
public class PartnerSimpleResDto {

    @Schema(description = "업체 고유 식별자", example = "1")
    private final Long id;

    @Schema(description = "업체명", example = "(주)컴퍼니")
    private final String partnerName;

    @Schema(description = "사업장번호", example = "250915-0617")
    private final String businessNumber;

    @Schema(description = "생성일자")
    private final LocalDateTime createdAt;

    @QueryProjection
    public PartnerSimpleResDto(Long id, String partnerName, String businessNumber, LocalDateTime createdAt) {
        this.id = id;
        this.partnerName = partnerName;
        this.businessNumber = businessNumber;
        this.createdAt = createdAt;
    }
}
