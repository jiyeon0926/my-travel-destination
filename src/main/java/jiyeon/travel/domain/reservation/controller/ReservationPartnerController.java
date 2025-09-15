package jiyeon.travel.domain.reservation.controller;

import jakarta.validation.Valid;
import jiyeon.travel.domain.reservation.dto.ReservationSimpleResDto;
import jiyeon.travel.domain.reservation.dto.ReservationStatusReqDto;
import jiyeon.travel.domain.reservation.service.ReservationPartnerService;
import jiyeon.travel.global.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partners/reservations")
@RequiredArgsConstructor
public class ReservationPartnerController {

    private final ReservationPartnerService reservationPartnerService;

    @PatchMapping("/{reservationId}/status")
    public ResponseEntity<ReservationSimpleResDto> changeReservationStatusById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                               @PathVariable Long reservationId,
                                                                               @Valid @RequestBody ReservationStatusReqDto reservationStatusReqDto) {
        String email = userDetails.getUsername();
        ReservationSimpleResDto reservationSimpleResDto = reservationPartnerService.changeReservationStatusById(
                email,
                reservationId,
                reservationStatusReqDto.getStatus()
        );

        return new ResponseEntity<>(reservationSimpleResDto, HttpStatus.OK);
    }
}
