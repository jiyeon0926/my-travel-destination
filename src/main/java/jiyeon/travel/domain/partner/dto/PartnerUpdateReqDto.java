package jiyeon.travel.domain.partner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "업체 정보 수정 요청")
public class PartnerUpdateReqDto {

    @Schema(description = "업체명", example = "(주)컴퍼니")
    private final String name;

    @Schema(description = "전화번호", example = "03112345678")
    private final String phone;

    @Schema(description = "주소")
    private final String address;
}
