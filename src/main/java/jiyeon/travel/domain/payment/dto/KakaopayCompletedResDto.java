package jiyeon.travel.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class KakaopayCompletedResDto {

    private final String tid;
    private final String cid;

    @JsonProperty("partner_order_id")
    private final String reservationNumber;

    @JsonProperty("partner_user_id")
    private final String userId;

    @JsonProperty("payment_method_type")
    private final String paymentMethod;

    @JsonProperty("total")
    private final Integer amount;

    @JsonProperty("item_name")
    private final String ticketName;

    private final Integer quantity;

    @JsonProperty("approved_at")
    private final LocalDateTime approvedAt;
}
