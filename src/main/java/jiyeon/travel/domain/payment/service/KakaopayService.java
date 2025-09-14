package jiyeon.travel.domain.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jiyeon.travel.domain.payment.dto.KakaopayCompletedResDto;
import jiyeon.travel.domain.payment.dto.KakaopayReadyResDto;
import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaopayService {

    private final RestTemplate restTemplate;

    @Value("${kakao.pay.secret-key}")
    private String secretKey;

    @Value("${kakao.pay.cid}")
    private String cid;

    @Value("${kakao.pay.ready-url}")
    private String readyUrl;

    @Value("${kakao.pay.approve-url}")
    private String approveUrl;

    @Value("${kakao.pay.approval-redirect-url}")
    private String approvalRedirectUrl;

    @Value("${kakao.pay.cancel-redirect-url}")
    private String cancelRedirectUrl;

    @Value("${kakao.pay.fail-redirect-url}")
    private String failRedirectUrl;

    public KakaopayReadyResDto readyPayment(Reservation reservation, Ticket ticket) {
        approvalRedirectUrl = String.format(approvalRedirectUrl, reservation.getId());

        Map<String, String> body = readyPaymentBody(reservation, ticket);
        HttpHeaders headers = httpHeaders();
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(readyUrl, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomException(ErrorCode.PAYMENT_READY_API_ERROR);
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());
            String tid = root.path("tid").asText();
            String nextRedirectPcUrl = root.path("next_redirect_pc_url").asText();
            String createdAtStr = root.path("created_at").asText();

            LocalDateTime createdAt = LocalDateTime.parse(createdAtStr, DateTimeFormatter.ISO_DATE_TIME);

            return new KakaopayReadyResDto(tid, nextRedirectPcUrl, createdAt);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.PAYMENT_READY_RESPONSE_ERROR);
        }
    }

    public KakaopayCompletedResDto approvePayment(String requestTid, Reservation reservation, String pgToken) {
        Map<String, String> body = approvePaymentBody(requestTid, reservation, pgToken);
        HttpHeaders headers = httpHeaders();
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(approveUrl, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomException(ErrorCode.PAYMENT_APPROVE_API_ERROR);
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());
            String tid = root.path("tid").asText();
            String cid = root.path("cid").asText();
            String reservationName = root.path("partner_order_id").asText();
            String userId = root.path("partner_user_id").asText();
            String paymentMethod = root.path("payment_method_type").asText();
            Integer amount = root.path("amount").path("total").asInt();
            String ticketName = root.path("item_name").asText();
            Integer quantity = root.path("quantity").asInt();
            String approvedAtStr = root.path("approved_at").asText();

            LocalDateTime approvedAt = LocalDateTime.parse(approvedAtStr, DateTimeFormatter.ISO_DATE_TIME);

            return new KakaopayCompletedResDto(
                    tid,
                    cid,
                    reservationName,
                    userId,
                    paymentMethod,
                    amount,
                    ticketName,
                    quantity,
                    approvedAt
            );
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.PAYMENT_APPROVE_RESPONSE_ERROR);
        }
    }

    private Map<String, String> readyPaymentBody(Reservation reservation, Ticket ticket) {
        Map<String, String> body = new HashMap<>();
        body.put("cid", cid);
        body.put("partner_order_id", reservation.getReservationNumber());
        body.put("partner_user_id", reservation.getUser().getId().toString());
        body.put("item_name", ticket.getName());
        body.put("quantity", String.valueOf(reservation.getTotalQuantity()));
        body.put("total_amount", String.valueOf(reservation.getTotalAmount()));
        body.put("tax_free_amount", "0");
        body.put("approval_url", approvalRedirectUrl);
        body.put("cancel_url", cancelRedirectUrl);
        body.put("fail_url", failRedirectUrl);

        return body;
    }

    private Map<String, String> approvePaymentBody(String requestTid, Reservation reservation, String pgToken) {
        Map<String, String> body = new HashMap<>();
        body.put("cid", cid);
        body.put("tid", requestTid);
        body.put("partner_order_id", reservation.getReservationNumber());
        body.put("partner_user_id", reservation.getUser().getId().toString());
        body.put("pg_token", pgToken);

        return body;
    }

    private HttpHeaders httpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "SECRET_KEY" + " " + secretKey);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return headers;
    }
}
