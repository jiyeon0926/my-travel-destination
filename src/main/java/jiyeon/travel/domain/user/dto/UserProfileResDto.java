package jiyeon.travel.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jiyeon.travel.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Schema(description = "사용자 프로필 응답")
public class UserProfileResDto {

    @Schema(description = "사용자 고유 식별자", example = "1")
    private final Long id;

    @Schema(description = "이메일", example = "user1@naver.com")
    private final String email;

    @Schema(description = "별명", example = "고양이")
    private final String nickname;

    @Schema(description = "전화번호", example = "01012349876")
    private final String phone;

    @Schema(description = "생성일자")
    private final LocalDateTime createdAt;

    @Schema(description = "수정일자")
    private final LocalDateTime updatedAt;

    public UserProfileResDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getDisplayName();
        this.phone = user.getPhone();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
