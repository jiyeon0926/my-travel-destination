package jiyeon.travel.domain.partner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "업체 회원가입 요청")
public class PartnerSignupReqDto {

    @Schema(description = "이메일", example = "partner1@naver.com")
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private final String email;

    @Schema(description = "비밀번호")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private final String password;

    @Schema(description = "업체명", example = "(주)컴퍼니")
    @NotBlank(message = "업체명은 필수입니다.")
    private final String name;

    @Schema(description = "사업장번호", example = "250915-0617")
    @NotBlank(message = "사업장번호는 필수입니다.")
    private final String businessNumber;

    @Schema(description = "주소")
    @NotBlank(message = "주소는 필수입니다.")
    private final String address;

    @Schema(description = "전화번호", example = "03112345678")
    @NotBlank(message = "전화번호는 필수입니다.")
    @Pattern(
            regexp = "^[0-9]{10,11}$",
            message = "전화번호는 10자리 또는 11자리만 가능하고, 숫자만 포함해야 합니다."
    )
    private final String phone;
}
