package jiyeon.travel.domain.partner.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PartnerListResDto {

    private final Long userId;
    private final Long partnerId;
    private final String partnerName;
    private final String businessNumber;
    private final LocalDateTime createdAt;

    @QueryProjection
    public PartnerListResDto(Long userId, Long partnerId, String partnerName, String businessNumber, LocalDateTime createdAt) {
        this.userId = userId;
        this.partnerId = partnerId;
        this.partnerName = partnerName;
        this.businessNumber = businessNumber;
        this.createdAt = createdAt;
    }
}
