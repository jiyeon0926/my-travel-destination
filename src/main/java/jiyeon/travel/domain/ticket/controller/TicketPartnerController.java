package jiyeon.travel.domain.ticket.controller;

import jakarta.validation.Valid;
import jiyeon.travel.domain.ticket.dto.*;
import jiyeon.travel.domain.ticket.service.TicketPartnerService;
import jiyeon.travel.global.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/partners/tickets")
@RequiredArgsConstructor
public class TicketPartnerController {

    private final TicketPartnerService ticketPartnerService;

    @PostMapping
    public ResponseEntity<TicketDetailResDto> createTicket(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @Valid @RequestPart("ticket") TicketCreateReqDto ticketCreateReqDto,
                                                           @RequestPart(value = "images", required = false) List<MultipartFile> files) {
        String email = userDetails.getUsername();
        TicketDetailResDto ticketDetailResDto = ticketPartnerService.createTicket(
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

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> deleteTicketById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @PathVariable Long ticketId) {
        String email = userDetails.getUsername();
        ticketPartnerService.deleteTicketById(ticketId, email);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{ticketId}")
    public ResponseEntity<TicketInfoDetailResDto> updateTicketInfoById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                       @PathVariable Long ticketId,
                                                                       @RequestBody TicketInfoUpdateReqDto ticketInfoUpdateReqDto) {
        String email = userDetails.getUsername();
        TicketInfoDetailResDto ticketInfoResDto = ticketPartnerService.updateTicketInfoById(
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

    @PostMapping("/{ticketId}/schedules")
    public ResponseEntity<TicketScheduleDetailResDto> addScheduleById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                      @PathVariable Long ticketId,
                                                                      @Valid @RequestBody TicketScheduleCreateReqDto ticketScheduleCreateReqDto) {
        String email = userDetails.getUsername();
        TicketScheduleDetailResDto ticketScheduleDetailResDto = ticketPartnerService.addScheduleById(
                email,
                ticketId,
                ticketScheduleCreateReqDto.getStartDate(),
                ticketScheduleCreateReqDto.getStartTime(),
                ticketScheduleCreateReqDto.getQuantity()
        );

        return new ResponseEntity<>(ticketScheduleDetailResDto, HttpStatus.CREATED);
    }

    @PostMapping("/{ticketId}/images")
    public ResponseEntity<List<TicketImageDetailResDto>> addImageById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                      @PathVariable Long ticketId,
                                                                      @RequestParam("images") List<MultipartFile> files) {
        String email = userDetails.getUsername();
        List<TicketImageDetailResDto> ticketImageDetailResDtos = ticketPartnerService.addImageById(email, ticketId, files);

        return new ResponseEntity<>(ticketImageDetailResDtos, HttpStatus.CREATED);
    }

    @DeleteMapping("/{ticketId}/images/{imageId}")
    public ResponseEntity<Void> deleteImageById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @PathVariable Long ticketId,
                                                @PathVariable Long imageId) {
        String email = userDetails.getUsername();
        ticketPartnerService.deleteImageById(email, ticketId, imageId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{ticketId}/images/{imageId}/main")
    public ResponseEntity<TicketImageDetailResDto> changeImageMainById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                       @PathVariable Long ticketId,
                                                                       @PathVariable Long imageId) {
        String email = userDetails.getUsername();
        TicketImageDetailResDto ticketImageDetailResDto = ticketPartnerService.changeImageMainById(email, ticketId, imageId);

        return new ResponseEntity<>(ticketImageDetailResDto, HttpStatus.OK);
    }
}
