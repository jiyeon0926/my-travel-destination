package jiyeon.travel.domain.partner.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PartnerUpdateReqDto {

    private final String name;
    private final String phone;
    private final String address;
}
