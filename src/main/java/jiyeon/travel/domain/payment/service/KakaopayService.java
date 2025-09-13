package jiyeon.travel.domain.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jiyeon.travel.domain.payment.dto.KakaopayCompletedResDto;
import jiyeon.travel.domain.payment.dto.KakaopayReadyResDto;
import jiyeon.travel.domain.payment.entity.Payment;
import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.domain.ticket.entity.Ticket;
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

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(response.getBody());
                String tid = root.path("tid").asText();
                String nextRedirectPcUrl = root.path("next_redirect_pc_url").asText();
                String createdAtStr = root.path("created_at").asText();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                LocalDateTime createdAt = LocalDateTime.parse(createdAtStr, formatter);

                return new KakaopayReadyResDto(tid, nextRedirectPcUrl, createdAt);
            } catch (Exception e) {
                throw new RuntimeException("카카오페이 결제 준비 응답 처리 중 오류가 발생하였습니다.");
            }
        } else {
            throw new RuntimeException("카카오페이 결제 준비 요청을 실패하였습니다.");
        }
    }

    public KakaopayCompletedResDto approvePayment(Payment payment, Reservation reservation, String pgToken) {
        Map<String, String> body = approvePaymentBody(payment, reservation, pgToken);
        HttpHeaders headers = httpHeaders();
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(approveUrl, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
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

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                LocalDateTime approvedAt = LocalDateTime.parse(approvedAtStr, formatter);

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
            } catch (Exception e) {
                throw new RuntimeException("카카오페이 결제 승인 응답 처리 중 오류가 발생하였습니다.");
            }
        } else {
            throw new RuntimeException("카카오페이 결제 승인 요청을 실패하였습니다.");
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

    private Map<String, String> approvePaymentBody(Payment payment, Reservation reservation, String pgToken) {
        Map<String, String> body = new HashMap<>();
        body.put("cid", cid);
        body.put("tid", payment.getTid());
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
