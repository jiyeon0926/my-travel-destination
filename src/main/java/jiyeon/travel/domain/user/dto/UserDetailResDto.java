package jiyeon.travel.domain.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(description = "사용자 상세 정보 응답 DTO")
public class UserDetailResDto {

    @Schema(description = "고유 식별자", example = "1")
    private final Long id;

    @Schema(description = "이메일", example = "user1@naver.com")
    private final String email;

    @Schema(description = "별명", example = "고양이")
    private final String nickname;

    @Schema(description = "전화번호", example = "01012349876")
    private final String phone;

    @Schema(description = "탈퇴 여부", example = "true")
    private final Boolean isDeleted;

    @Schema(description = "생성일자")
    private final LocalDateTime createdAt;

    @Schema(description = "수정일자")
    private final LocalDateTime updatedAt;

    @QueryProjection
    public UserDetailResDto(Long id, String email, String nickname, String phone, boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.phone = phone;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
