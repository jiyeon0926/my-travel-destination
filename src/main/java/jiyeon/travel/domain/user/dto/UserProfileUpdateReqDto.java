package jiyeon.travel.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "프로필 수정 요청 DTO")
public class UserProfileUpdateReqDto {

    @Schema(description = "별명", example = "고양이")
    private final String nickname;

    @Schema(description = "전화번호", example = "01012349876")
    private final String phone;
}
