package jiyeon.travel.domain.reservation.controller;

import jakarta.validation.Valid;
import jiyeon.travel.domain.reservation.dto.ReservationDetailResDto;
import jiyeon.travel.domain.reservation.dto.ReservationSimpleResDto;
import jiyeon.travel.domain.reservation.dto.ReservationStatusReqDto;
import jiyeon.travel.domain.reservation.service.ReservationPartnerService;
import jiyeon.travel.global.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partners/reservations")
@RequiredArgsConstructor
public class ReservationPartnerController {

    private final ReservationPartnerService reservationPartnerService;

    @GetMapping
    public ResponseEntity<List<ReservationSimpleResDto>> findAllWithoutUnpaid(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUsername();
        List<ReservationSimpleResDto> reservationSimpleResDtos = reservationPartnerService.findAllWithoutUnpaid(email);

        return new ResponseEntity<>(reservationSimpleResDtos, HttpStatus.OK);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationDetailResDto> findReservationById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                       @PathVariable Long reservationId) {
        String email = userDetails.getUsername();
        ReservationDetailResDto reservationDetailResDto = reservationPartnerService.findReservationById(email, reservationId);

        return new ResponseEntity<>(reservationDetailResDto, HttpStatus.OK);
    }

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
