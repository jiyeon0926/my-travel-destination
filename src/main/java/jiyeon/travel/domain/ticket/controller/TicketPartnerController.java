package jiyeon.travel.domain.ticket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jiyeon.travel.domain.ticket.dto.*;
import jiyeon.travel.domain.ticket.service.TicketPartnerCommandService;
import jiyeon.travel.domain.ticket.service.TicketPartnerQueryService;
import jiyeon.travel.global.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/partners/tickets")
@RequiredArgsConstructor
@Tag(
        name = "Partner Ticket",
        description = "업체가 티켓을 등록, 수정, 삭제, 조회 및 검색할 수 있는 기능을 제공합니다."
)
public class TicketPartnerController {

    private final TicketPartnerCommandService ticketPartnerCommandService;
    private final TicketPartnerQueryService ticketPartnerQueryService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "티켓 등록", description = "업체는 티켓명, 판매 기간, 전화번호, 주소, 가격, 설명, 옵션, 일정, 이미지를 포함한 티켓을 등록할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketDetailResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "업체 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<TicketDetailResDto> createTicket(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @Valid @RequestPart("ticket") TicketCreateReqDto ticketCreateReqDto,
                                                           @RequestPart(value = "images", required = false) List<MultipartFile> files) {
        String email = userDetails.getUsername();
        TicketDetailResDto ticketDetailResDto = ticketPartnerCommandService.createTicket(
                email,
                ticketCreateReqDto.getName(),
                ticketCreateReqDto.getSaleStartDate(),
                ticketCreateReqDto.getSaleEndDate(),
                ticketCreateReqDto.getPhone(),
                ticketCreateReqDto.getAddress(),
                ticketCreateReqDto.getBasePrice(),
                ticketCreateReqDto.getDescription(),
                ticketCreateReqDto.getOptions(),
                ticketCreateReqDto.getSchedules(),
                files
        );

        return new ResponseEntity<>(ticketDetailResDto, HttpStatus.CREATED);
    }

    @PostMapping("/{ticketId}/options")
    @Operation(summary = "티켓 옵션 추가", description = "업체는 티켓의 판매 상태가 판매 전일 때만 티켓에 새로운 옵션을 추가할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketOptionDetailResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "업체 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "티켓을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<TicketOptionDetailResDto> addOptionById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                  @PathVariable Long ticketId,
                                                                  @Valid @RequestBody TicketOptionCreateReqDto ticketOptionCreateReqDto) {
        String email = userDetails.getUsername();
        TicketOptionDetailResDto ticketOptionDetailResDto = ticketPartnerCommandService.addOptionById(
                email,
                ticketId,
                ticketOptionCreateReqDto.getName(),
                ticketOptionCreateReqDto.getPrice()
        );

        return new ResponseEntity<>(ticketOptionDetailResDto, HttpStatus.CREATED);
    }

    @PostMapping("/{ticketId}/schedules")
    @Operation(summary = "티켓 일정 추가", description = "업체는 티켓의 판매 상태가 판매 전일 때만 티켓에 새로운 일정을 추가할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketScheduleDetailResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "업체 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "티켓을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<TicketScheduleDetailResDto> addScheduleById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                      @PathVariable Long ticketId,
                                                                      @Valid @RequestBody TicketScheduleCreateReqDto ticketScheduleCreateReqDto) {
        String email = userDetails.getUsername();
        TicketScheduleDetailResDto ticketScheduleDetailResDto = ticketPartnerCommandService.addScheduleById(
                email,
                ticketId,
                ticketScheduleCreateReqDto.getStartDate(),
                ticketScheduleCreateReqDto.getStartTime(),
                ticketScheduleCreateReqDto.getQuantity()
        );

        return new ResponseEntity<>(ticketScheduleDetailResDto, HttpStatus.CREATED);
    }

    @PostMapping("/{ticketId}/images")
    @Operation(summary = "티켓 이미지 추가", description = "업체는 티켓의 판매 상태가 판매 전이거나 판매 중일 때만 업체는 티켓에 새로운 이미지를 추가할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketImageDetailsResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "업체 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "티켓을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<TicketImageDetailsResDto> addImageById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @PathVariable Long ticketId,
                                                                 @RequestParam("images") List<MultipartFile> files) {
        String email = userDetails.getUsername();
        TicketImageDetailsResDto ticketImageDetailsResDto = ticketPartnerCommandService.addImageById(email, ticketId, files);

        return new ResponseEntity<>(ticketImageDetailsResDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{ticketId}")
    @Operation(summary = "티켓 정보 수정", description = "업체는 티켓의 판매 상태가 판매 전일 때만 티켓명, 판매 기간, 전화번호, 주소, 가격, 설명을 수정할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketSimpleResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "업체 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "티켓을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<TicketSimpleResDto> updateTicketInfoById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                   @PathVariable Long ticketId,
                                                                   @RequestBody TicketInfoUpdateReqDto ticketInfoUpdateReqDto) {
        String email = userDetails.getUsername();
        TicketSimpleResDto ticketInfoResDto = ticketPartnerCommandService.updateTicketInfoById(
                ticketId,
                email,
                ticketInfoUpdateReqDto.getName(),
                ticketInfoUpdateReqDto.getSaleStartDate(),
                ticketInfoUpdateReqDto.getSaleEndDate(),
                ticketInfoUpdateReqDto.getPhone(),
                ticketInfoUpdateReqDto.getAddress(),
                ticketInfoUpdateReqDto.getBasePrice(),
                ticketInfoUpdateReqDto.getDescription()
        );

        return new ResponseEntity<>(ticketInfoResDto, HttpStatus.OK);
    }

    @PatchMapping("/{ticketId}/status")
    @Operation(summary = "티켓 상태 변경", description = "업체는 티켓 판매 상태를 변경할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketSimpleResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "업체 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "티켓을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<TicketSimpleResDto> changeTicketStatusById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                     @PathVariable Long ticketId,
                                                                     @Valid @RequestBody TicketStatusReqDto ticketStatusReqDto) {
        String email = userDetails.getUsername();
        TicketSimpleResDto ticketSimpleResDto = ticketPartnerCommandService.changeTicketStatusById(email, ticketId, ticketStatusReqDto.getSaleStatus());

        return new ResponseEntity<>(ticketSimpleResDto, HttpStatus.OK);
    }

    @PatchMapping("/{ticketId}/options/{optionId}")
    @Operation(summary = "티켓 옵션 수정", description = "업체는 티켓의 판매 상태가 판매 전일 때만 옵션을 수정할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketOptionDetailResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "업체 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "티켓 옵션을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<TicketOptionDetailResDto> updateOptionById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                     @PathVariable Long ticketId,
                                                                     @PathVariable Long optionId,
                                                                     @RequestBody TicketOptionUpdateReqDto ticketOptionUpdateReqDto) {
        String email = userDetails.getUsername();
        TicketOptionDetailResDto ticketOptionDetailResDto = ticketPartnerCommandService.updateOptionById(
                email,
                ticketId,
                optionId,
                ticketOptionUpdateReqDto.getName(),
                ticketOptionUpdateReqDto.getPrice()
        );

        return new ResponseEntity<>(ticketOptionDetailResDto, HttpStatus.OK);
    }

    @PatchMapping("/{ticketId}/schedules/{scheduleId}")
    @Operation(summary = "티켓 일정 수정", description = "업체는 티켓의 판매 상태가 판매 전일 때만 일정을 수정할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketScheduleDetailResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "업체 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "티켓 일정을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<TicketScheduleDetailResDto> updateScheduleById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                         @PathVariable Long ticketId,
                                                                         @PathVariable Long scheduleId,
                                                                         @Valid @RequestBody TicketScheduleUpdateReqDto ticketScheduleUpdateReqDto) {
        String email = userDetails.getUsername();
        TicketScheduleDetailResDto ticketScheduleDetailResDto = ticketPartnerCommandService.updateScheduleById(
                email,
                ticketId,
                scheduleId,
                ticketScheduleUpdateReqDto.getStartDate(),
                ticketScheduleUpdateReqDto.getStartTime(),
                ticketScheduleUpdateReqDto.getIsActive(),
                ticketScheduleUpdateReqDto.getQuantity()
        );

        return new ResponseEntity<>(ticketScheduleDetailResDto, HttpStatus.OK);
    }

    @PatchMapping("/{ticketId}/images/{imageId}/main")
    @Operation(summary = "티켓 대표 이미지 변경", description = "업체는 티켓의 판매 상태가 판매 전이거나 판매 중일 때만 업로드된 이미지 중 하나를 선택하여 티켓 대표 이미지로 설정할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketImageDetailResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "업체 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "티켓 이미지를 찾을 수 없습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "이미 대표 이미지입니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<TicketImageDetailResDto> changeImageMainById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                       @PathVariable Long ticketId,
                                                                       @PathVariable Long imageId) {
        String email = userDetails.getUsername();
        TicketImageDetailResDto ticketImageDetailResDto = ticketPartnerCommandService.changeImageMainById(email, ticketId, imageId);

        return new ResponseEntity<>(ticketImageDetailResDto, HttpStatus.OK);
    }

    @DeleteMapping("/{ticketId}")
    @Operation(summary = "티켓 삭제", description = "업체는 티켓의 판매 상태가 판매 전일 때만 티켓을 삭제할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "업체 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "티켓을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> deleteTicketById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @PathVariable Long ticketId) {
        String email = userDetails.getUsername();
        ticketPartnerCommandService.deleteTicketById(ticketId, email);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{ticketId}/options/{optionId}")
    @Operation(summary = "티켓 옵션 삭제", description = "업체는 티켓의 판매 상태가 판매 전일 때만 티켓 옵션을 삭제할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "업체 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "티켓 옵션을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> deleteOptionById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @PathVariable Long ticketId,
                                                 @PathVariable Long optionId) {
        String email = userDetails.getUsername();
        ticketPartnerCommandService.deleteOptionById(email, ticketId, optionId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{ticketId}/schedules/{scheduleId}")
    @Operation(summary = "티켓 일정 삭제", description = "업체는 티켓의 판매 상태가 판매 전일 때만 티켓 일정을 삭제할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "업체 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "티켓 일정을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> deleteScheduleById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @PathVariable Long ticketId,
                                                   @PathVariable Long scheduleId) {
        String email = userDetails.getUsername();
        ticketPartnerCommandService.deleteScheduleById(email, ticketId, scheduleId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{ticketId}/images/{imageId}")
    @Operation(summary = "티켓 이미지 삭제", description = "업체는 티켓의 판매 상태가 판매 전이거나 판매 중일 때만 티켓 이미지를 삭제할 수 있습니다. 대표 이미지는 삭제할 수 없습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "업체 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "티켓 이미지를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> deleteImageById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @PathVariable Long ticketId,
                                                @PathVariable Long imageId) {
        String email = userDetails.getUsername();
        ticketPartnerCommandService.deleteImageById(email, ticketId, imageId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{ticketId}")
    @Operation(summary = "업체 티켓 상세 조회", description = "업체가 등록한 티켓의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketDetailResDto.class))),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "업체 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "티켓을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<TicketDetailResDto> findMyTicketById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @PathVariable Long ticketId) {
        String email = userDetails.getUsername();
        TicketDetailResDto ticketDetailResDto = ticketPartnerQueryService.findMyTicketById(email, ticketId);

        return new ResponseEntity<>(ticketDetailResDto, HttpStatus.OK);
    }

    @GetMapping("/search")
    @Operation(summary = "업체 티켓 검색", description = "업체는 판매 상태를 기준으로 등록한 티켓을 검색할 수 있습니다.")
    @Parameter(name = "saleStatus", description = "티켓 판매 상태")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketListResDto.class))),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "업체 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<TicketListResDto> searchMyTickets(@RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(required = false) String saleStatus,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUsername();
        TicketListResDto ticketListResDto = ticketPartnerQueryService.searchMyTickets(page, size, saleStatus, email);

        return new ResponseEntity<>(ticketListResDto, HttpStatus.OK);
    }
}
