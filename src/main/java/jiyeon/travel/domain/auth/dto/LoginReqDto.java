package jiyeon.travel.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "로그인 요청")
public class LoginReqDto {

    @Schema(description = "이메일", example = "user1@naver.com")
    @NotBlank(message = "이메일은 필수입니다.")
    private final String email;

    @Schema(description = "비밀번호")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private final String password;
}
