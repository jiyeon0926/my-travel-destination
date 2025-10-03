package jiyeon.travel.domain.partner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jiyeon.travel.domain.partner.entity.Partner;
import jiyeon.travel.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Schema(description = "업체 회원가입 응답")
public class PartnerSignupResDto {

    @Schema(description = "업체 고유 식별자", example = "1")
    private final Long id;

    @Schema(description = "이메일", example = "partner1@naver.com")
    private final String email;

    @Schema(description = "업체명", example = "(주)컴퍼니")
    private final String partnerName;

    @Schema(description = "사업장번호", example = "250915-0617")
    private final String businessNumber;

    @Schema(description = "주소")
    private final String address;

    @Schema(description = "전화번호", example = "03112345678")
    private final String phone;

    @Schema(description = "권한", example = "PARTNER")
    private final String role;

    @Schema(description = "생성일자")
    private final LocalDateTime createdAt;

    @Schema(description = "수정일자")
    private final LocalDateTime updatedAt;

    public PartnerSignupResDto(User user, Partner partner) {
        this.id = partner.getId();
        this.email = user.getEmail();
        this.partnerName = user.getDisplayName();
        this.businessNumber = partner.getBusinessNumber();
        this.address = partner.getAddress();
        this.phone = user.getPhone();
        this.role = user.getRole().name();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
