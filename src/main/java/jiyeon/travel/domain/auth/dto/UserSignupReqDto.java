package jiyeon.travel.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "사용자 회원가입 요청")
public class UserSignupReqDto {

    @Schema(description = "이메일", example = "user1@naver.com")
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private final String email;

    @Schema(description = "최소 8자 이상, 대문자, 소문자, 숫자, 특수문자를 포함한 비밀번호")
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 최소 8자 이상, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다."
    )
    private final String password;

    @Schema(description = "별명", example = "고양이")
    @NotBlank(message = "별명은 필수입니다.")
    private final String nickname;

    @Schema(description = "전화번호", example = "01012349876")
    @NotBlank(message = "전화번호는 필수입니다.")
    private final String phone;
}
