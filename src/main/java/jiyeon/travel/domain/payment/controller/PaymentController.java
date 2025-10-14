package jiyeon.travel.domain.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jiyeon.travel.domain.payment.dto.KakaopayCompletedResDto;
import jiyeon.travel.domain.payment.dto.KakaopayReadyResDto;
import jiyeon.travel.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations/{reservationId}/payments")
@RequiredArgsConstructor
@Tag(
        name = "Payment",
        description = "사용자가 예약된 티켓을 결제할 수 있는 기능을 제공합니다."
)
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/ready")
    @Operation(summary = "카카오페이 결제 준비", description = "사용자가 카카오페이 결제를 요청하면 결제 고유 번호와 결제 페이지로 이동할 수 있는 URL을 응답받습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = KakaopayReadyResDto.class))),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "사용자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "이미 결제된 예약입니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<KakaopayReadyResDto> readyPayment(@PathVariable Long reservationId) {
        KakaopayReadyResDto kakaopayReadyResDto = paymentService.readyPayment(reservationId);

        return new ResponseEntity<>(kakaopayReadyResDto, HttpStatus.OK);
    }

    @GetMapping("/completed")
    @Operation(summary = "카카오페이 결제 승인 완료", description = "PG 토큰을 이용해 결제를 승인합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = KakaopayCompletedResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "결제를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<KakaopayCompletedResDto> completedPayment(@PathVariable Long reservationId,
                                                                    @RequestParam(value = "pg_token") String pgToken) {
        KakaopayCompletedResDto kakaopayCompletedResDto = paymentService.completedPayment(reservationId, pgToken);

        return new ResponseEntity<>(kakaopayCompletedResDto, HttpStatus.OK);
    }
}
