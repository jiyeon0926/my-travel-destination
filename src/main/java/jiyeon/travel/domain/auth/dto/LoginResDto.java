package jiyeon.travel.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "로그인 응답")
public class LoginResDto {

    @Schema(description = "토큰 인증 타입", example = "Bearer")
    private final String tokenAuthScheme;

    @Schema(description = "인증 토큰")
    private final String accessToken;
}
