package jiyeon.travel.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "사용자 프로필 수정 요청")
public class UserProfileUpdateReqDto {

    @Schema(description = "별명", example = "고양이")
    private final String nickname;

    @Schema(description = "전화번호", example = "01012349876")
    @Pattern(
            regexp = "^[0-9]{10,11}$",
            message = "전화번호는 10자리 또는 11자리만 가능하고, 숫자만 포함해야 합니다."
    )
    private final String phone;
}
