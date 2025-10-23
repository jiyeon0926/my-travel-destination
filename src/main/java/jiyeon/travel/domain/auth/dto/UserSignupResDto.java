package jiyeon.travel.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jiyeon.travel.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "사용자 회원가입 응답")
public class UserSignupResDto {

    @Schema(description = "사용자 고유 식별자", example = "1")
    private final Long id;

    @Schema(description = "이메일", example = "user1@naver.com")
    private final String email;

    @Schema(description = "별명", example = "고양이")
    private final String nickname;

    @Schema(description = "전화번호", example = "01012349876")
    private final String phone;

    @Schema(description = "권한", example = "USER")
    private final String role;

    public UserSignupResDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getDisplayName();
        this.phone = user.getPhone();
        this.role = user.getRole().name();
    }
}
