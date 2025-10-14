package jiyeon.travel.domain.reservation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jiyeon.travel.domain.reservation.dto.ReservationCreateReqDto;
import jiyeon.travel.domain.reservation.dto.ReservationDetailResDto;
import jiyeon.travel.domain.reservation.dto.ReservationSimpleResDto;
import jiyeon.travel.domain.reservation.service.ReservationFacade;
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
@Tag(
        name = "User Reservation",
        description = "사용자가 티켓을 예약하고 조회할 수 있는 기능을 제공합니다."
)
public class ReservationController {

    private final ReservationFacade reservationFacade;

    @PostMapping
    @Operation(summary = "티켓 예약", description = "사용자가 티켓 일정과 수량을 선택하여 예약할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationDetailResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "사용자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "티켓 일정 또는 티켓을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<ReservationDetailResDto> createReservation(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                     @Valid @RequestBody ReservationCreateReqDto reservationCreateReqDto) {
        String email = userDetails.getUsername();
        ReservationDetailResDto reservationDetailResDto = reservationFacade.createReservationWithLock(
                email,
                reservationCreateReqDto.getScheduleId(),
                reservationCreateReqDto.getBaseQuantity(),
                reservationCreateReqDto.getReservationName(),
                reservationCreateReqDto.getReservationPhone(),
                reservationCreateReqDto.getOptions()
        );

        return new ResponseEntity<>(reservationDetailResDto, HttpStatus.CREATED);
    }

    @GetMapping("/{reservationId}")
    @Operation(summary = "사용자 티켓 예약 상세 조회", description = "사용자는 본인의 예약 상세 정보를 조회할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationDetailResDto.class))),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "사용자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<ReservationDetailResDto> findMyReservationById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                         @PathVariable Long reservationId) {
        String email = userDetails.getUsername();
        ReservationDetailResDto reservationDetailResDto = reservationFacade.findMyReservationById(email, reservationId);

        return new ResponseEntity<>(reservationDetailResDto, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "사용자 티켓 예약 전체 조회", description = "사용자는 예약 내역을 모두 조회할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ReservationSimpleResDto.class))
                    )),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "사용자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<ReservationSimpleResDto>> findAll(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUsername();
        List<ReservationSimpleResDto> reservationSimpleResDtoList = reservationFacade.findAll(email);

        return new ResponseEntity<>(reservationSimpleResDtoList, HttpStatus.OK);
    }

    @GetMapping("/used")
    @Operation(summary = "사용 완료된 예약 전체 조회", description = "사용자는 사용 완료된 예약을 모두 조회할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ReservationSimpleResDto.class))
                    )),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "사용자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<ReservationSimpleResDto>> findMyUsedReservations(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUsername();
        List<ReservationSimpleResDto> reservationSimpleResDtoList = reservationFacade.findMyUsedReservations(email);

        return new ResponseEntity<>(reservationSimpleResDtoList, HttpStatus.OK);
    }
}
