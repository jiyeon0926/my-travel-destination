package jiyeon.travel.domain.ticket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jiyeon.travel.domain.ticket.dto.TicketDetailWithBlogResDto;
import jiyeon.travel.domain.ticket.dto.TicketListResDto;
import jiyeon.travel.domain.ticket.dto.TicketOptionDetailsResDto;
import jiyeon.travel.domain.ticket.dto.TicketScheduleDetailsResDto;
import jiyeon.travel.domain.ticket.service.TicketQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Tag(
        name = "User Ticket",
        description = "사용자가 티켓을 검색하고 상세 정보를 조회할 수 있는 기능을 제공합니다."
)
public class TicketController {

    private final TicketQueryService ticketQueryService;

    @GetMapping("/{ticketId}")
    @Operation(summary = "티켓 상세 조회", description = "사용자는 해당 티켓의 상세 정보와 관련된 블로그를 조회할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketDetailWithBlogResDto.class))),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "사용자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "티켓을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<TicketDetailWithBlogResDto> findTicketById(@PathVariable Long ticketId,
                                                                     @RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int size) {
        TicketDetailWithBlogResDto ticketDetailWithBlogResDto = ticketQueryService.findTicketById(page, size, ticketId);

        return new ResponseEntity<>(ticketDetailWithBlogResDto, HttpStatus.OK);
    }

    @GetMapping("/{ticketId}/options")
    @Operation(summary = "티켓 옵션 전체 조회", description = "사용자가 티켓 예약 시 선택할 수 있는 모든 옵션을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketOptionDetailsResDto.class))),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "사용자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "티켓을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<TicketOptionDetailsResDto> getBaseOrOptionById(@PathVariable Long ticketId) {
        TicketOptionDetailsResDto ticketOptionDetailsResDto = ticketQueryService.getBaseOrOptionById(ticketId);

        return new ResponseEntity<>(ticketOptionDetailsResDto, HttpStatus.OK);
    }

    @GetMapping("/{ticketId}/schedules")
    @Operation(summary = "티켓 일정 전체 조회", description = "사용자가 티켓 예약 시 선택할 수 있는 모든 일정을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketScheduleDetailsResDto.class))),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "사용자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "티켓을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<TicketScheduleDetailsResDto> getScheduleById(@PathVariable Long ticketId) {
        TicketScheduleDetailsResDto ticketScheduleDetailsResDto = ticketQueryService.getScheduleById(ticketId);

        return new ResponseEntity<>(ticketScheduleDetailsResDto, HttpStatus.OK);
    }

    @GetMapping("/search")
    @Operation(summary = "티켓 검색", description = "티켓명을 기준으로 티켓을 검색할 수 있습니다.")
    @Parameter(name = "name", description = "티켓명")
    @ApiResponse(
            responseCode = "200", description = "요청에 성공하였습니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketListResDto.class))
    )
    public ResponseEntity<TicketListResDto> searchTickets(@RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(required = false) String name) {
        TicketListResDto ticketListResDto = ticketQueryService.searchTickets(page, size, name);

        return new ResponseEntity<>(ticketListResDto, HttpStatus.OK);
    }
}
