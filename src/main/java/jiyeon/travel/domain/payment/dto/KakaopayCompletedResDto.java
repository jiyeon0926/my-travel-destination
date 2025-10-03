package jiyeon.travel.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Schema(description = "카카오페이 결제 승인 완료 응답")
public class KakaopayCompletedResDto {

    @Schema(description = "카카오페이 결제 고유 번호")
    private final String tid;

    @Schema(description = "가맹점 코드")
    private final String cid;

    @Schema(description = "예약 번호", example = "yyyyMMdd-UUID")
    @JsonProperty("partner_order_id")
    private final String reservationNumber;

    @Schema(description = "사용자 고유 식별자")
    @JsonProperty("partner_user_id")
    private final String userId;

    @Schema(description = "결제 수단", example = "MONEY")
    @JsonProperty("payment_method_type")
    private final String paymentMethod;

    @Schema(description = "결제 금액", example = "20000")
    @JsonProperty("total")
    private final Integer amount;

    @Schema(description = "티켓명")
    @JsonProperty("item_name")
    private final String ticketName;

    @Schema(description = "수량", example = "3")
    private final Integer quantity;

    @Schema(description = "카카오페이 결제 승인일자")
    @JsonProperty("approved_at")
    private final LocalDateTime approvedAt;
}
