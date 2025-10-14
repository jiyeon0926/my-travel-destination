package jiyeon.travel.domain.reservation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jiyeon.travel.domain.reservation.dto.ReservationDetailResDto;
import jiyeon.travel.domain.reservation.dto.ReservationSimpleResDto;
import jiyeon.travel.domain.reservation.dto.ReservationStatusReqDto;
import jiyeon.travel.domain.reservation.service.ReservationPartnerCommandService;
import jiyeon.travel.domain.reservation.service.ReservationPartnerQueryService;
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
@Tag(
        name = "Partner Reservation",
        description = "업체가 결제 완료된 예약을 조회하고, 해당 예약의 상태를 사용 완료 또는 노쇼로 변경할 수 있는 기능을 제공합니다."
)
public class ReservationPartnerController {

    private final ReservationPartnerCommandService reservationPartnerCommandService;
    private final ReservationPartnerQueryService reservationPartnerQueryService;

    @PatchMapping("/{reservationId}/status")
    @Operation(summary = "티켓 예약 상태 변경", description = "업체가 결제 완료된 예약에 대해 사용 완료 또는 노쇼로 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationSimpleResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "업체 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<ReservationSimpleResDto> changeReservationStatusById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                               @PathVariable Long reservationId,
                                                                               @Valid @RequestBody ReservationStatusReqDto reservationStatusReqDto) {
        String email = userDetails.getUsername();
        ReservationSimpleResDto reservationSimpleResDto = reservationPartnerCommandService.changeReservationStatusById(
                email,
                reservationId,
                reservationStatusReqDto.getStatus()
        );

        return new ResponseEntity<>(reservationSimpleResDto, HttpStatus.OK);
    }

    @GetMapping("/{reservationId}")
    @Operation(summary = "업체 티켓 예약 상세 조회", description = "업체는 결제 완료된 예약의 상세 정보를 조회할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationDetailResDto.class))),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "업체 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<ReservationDetailResDto> findReservationById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                       @PathVariable Long reservationId) {
        String email = userDetails.getUsername();
        ReservationDetailResDto reservationDetailResDto = reservationPartnerQueryService.findReservationById(email, reservationId);

        return new ResponseEntity<>(reservationDetailResDto, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "업체 티켓 예약 전체 조회", description = "업체는 결제 완료된 예약을 모두 조회할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ReservationSimpleResDto.class))
                    )),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "업체 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<List<ReservationSimpleResDto>> findAllWithoutUnpaid(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUsername();
        List<ReservationSimpleResDto> reservationSimpleResDtoList = reservationPartnerQueryService.findAllWithoutUnpaid(email);

        return new ResponseEntity<>(reservationSimpleResDtoList, HttpStatus.OK);
    }
}
