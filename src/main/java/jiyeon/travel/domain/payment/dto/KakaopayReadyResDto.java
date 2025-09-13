package jiyeon.travel.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class KakaopayReadyResDto {

    private final String tid;

    @JsonProperty("next_redirect_pc_url")
    private final String nextRedirectPcUrl;

    @JsonProperty("created_at")
    private final LocalDateTime createdAt;
}
