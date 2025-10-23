package jiyeon.travel.domain.partner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "업체 정보 수정 요청")
public class PartnerUpdateReqDto {

    @Schema(description = "업체명", example = "(주)컴퍼니")
    private final String name;

    @Schema(description = "전화번호", example = "03112345678")
    @Pattern(
            regexp = "^[0-9]{10,11}$",
            message = "전화번호는 10자리 또는 11자리만 가능하고, 숫자만 포함해야 합니다."
    )
    private final String phone;

    @Schema(description = "주소")
    private final String address;
}
