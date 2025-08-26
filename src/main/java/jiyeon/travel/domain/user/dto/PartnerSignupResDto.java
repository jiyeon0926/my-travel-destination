package jiyeon.travel.domain.user.dto;

import jiyeon.travel.domain.user.entity.Partner;
import jiyeon.travel.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PartnerSignupResDto {

    private final Long userId;
    private final Long partnerId;
    private final String email;
    private final String partnerName;
    private final String businessNumber;
    private final String address;
    private final String phone;
    private final String role;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public PartnerSignupResDto(User user, Partner partner) {
        this.userId = user.getId();
        this.partnerId = partner.getId();
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
