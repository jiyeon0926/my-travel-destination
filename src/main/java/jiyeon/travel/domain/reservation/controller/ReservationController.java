package jiyeon.travel.domain.reservation.controller;

import jakarta.validation.Valid;
import jiyeon.travel.domain.reservation.dto.ReservationCreateReqDto;
import jiyeon.travel.domain.reservation.dto.ReservationDetailResDto;
import jiyeon.travel.domain.reservation.dto.ReservationSimpleResDto;
import jiyeon.travel.domain.reservation.service.ReservationService;
import jiyeon.travel.global.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationDetailResDto> createReservation(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                     @Valid @RequestBody ReservationCreateReqDto reservationCreateReqDto) {
        String email = userDetails.getUsername();
        ReservationDetailResDto reservationDetailResDto = reservationService.createReservation(
                email,
                reservationCreateReqDto.getScheduleId(),
                reservationCreateReqDto.getBaseQuantity(),
                reservationCreateReqDto.getReservationName(),
                reservationCreateReqDto.getReservationPhone(),
                reservationCreateReqDto.getOptions()
        );

        return new ResponseEntity<>(reservationDetailResDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReservationSimpleResDto>> findAll(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUsername();
        List<ReservationSimpleResDto> reservationSimpleResDtos = reservationService.findAll(email);

        return new ResponseEntity<>(reservationSimpleResDtos, HttpStatus.OK);
    }

    @GetMapping("/used")
    public ResponseEntity<List<ReservationSimpleResDto>> findMyUsedReservations(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUsername();
        List<ReservationSimpleResDto> reservationSimpleResDtos = reservationService.findMyUsedReservations(email);

        return new ResponseEntity<>(reservationSimpleResDtos, HttpStatus.OK);
    }
}
