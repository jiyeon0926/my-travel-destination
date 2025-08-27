package jiyeon.travel.domain.partner.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PartnerSignupReqDto {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private final String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private final String password;

    @NotBlank(message = "업체명은 필수입니다.")
    private final String name;

    @NotBlank(message = "사업장번호는 필수입니다.")
    private final String businessNumber;

    @NotBlank(message = "주소는 필수입니다.")
    private final String address;

    @NotBlank(message = "전화번호는 필수입니다.")
    private final String phone;
}
