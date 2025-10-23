package jiyeon.travel.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "비밀번호 변경 요청")
public class PasswordUpdateReqDto {

    @Schema(description = "기존 비밀번호")
    @NotBlank(message = "기존 비밀번호는 필수입니다.")
    private final String oldPassword;

    @Schema(description = "최소 8자 이상, 대문자, 소문자, 숫자, 특수문자를 포함한 비밀번호")
    @NotBlank(message = "새 비밀번호는 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 최소 8자 이상, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다."
    )
    private final String newPassword;
}
