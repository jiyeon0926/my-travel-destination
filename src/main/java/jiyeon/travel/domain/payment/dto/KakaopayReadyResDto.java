package jiyeon.travel.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Schema(description = "카카오페이 결제 준비 응답")
public class KakaopayReadyResDto {

    @Schema(description = "카카오페이 결제 고유 번호")
    private final String tid;

    @Schema(description = "카카오톡 결제 페이지 Redirect URL")
    @JsonProperty("next_redirect_pc_url")
    private final String nextRedirectPcUrl;

    @Schema(description = "결제 준비 요청 시간")
    @JsonProperty("created_at")
    private final LocalDateTime createdAt;
}
