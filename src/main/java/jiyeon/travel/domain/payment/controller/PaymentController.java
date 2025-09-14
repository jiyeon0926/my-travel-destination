package jiyeon.travel.domain.payment.controller;

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
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/ready")
    public ResponseEntity<KakaopayReadyResDto> readyPayment(@PathVariable Long reservationId) {
        KakaopayReadyResDto kakaopayReadyResDto = paymentService.readyPayment(reservationId);

        return new ResponseEntity<>(kakaopayReadyResDto, HttpStatus.OK);
    }

    @GetMapping("/completed")
    public ResponseEntity<KakaopayCompletedResDto> completedPayment(@PathVariable Long reservationId,
                                                                    @RequestParam(value = "pg_token") String pgToken) {
        KakaopayCompletedResDto kakaopayCompletedResDto = paymentService.completedPayment(reservationId, pgToken);

        return new ResponseEntity<>(kakaopayCompletedResDto, HttpStatus.OK);
    }
}
