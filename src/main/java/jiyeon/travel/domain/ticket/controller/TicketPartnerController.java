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

    @GetMapping
    public ResponseEntity<TicketListResDto> findAllMyTickets(@RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUsername();
        TicketListResDto ticketListResDto = ticketPartnerService.findAllMyTickets(page, size, email);

        return new ResponseEntity<>(ticketListResDto, HttpStatus.OK);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketDetailResDto> findMyTicketById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @PathVariable Long ticketId) {
        String email = userDetails.getUsername();
        TicketDetailResDto ticketDetailResDto = ticketPartnerService.findMyTicketById(email, ticketId);

        return new ResponseEntity<>(ticketDetailResDto, HttpStatus.OK);
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

    @PostMapping("/{ticketId}/options")
    public ResponseEntity<TicketOptionDetailResDto> addOptionById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                  @PathVariable Long ticketId,
                                                                  @Valid @RequestBody TicketOptionCreateReqDto ticketOptionCreateReqDto) {
        String email = userDetails.getUsername();
        TicketOptionDetailResDto ticketOptionDetailResDto = ticketPartnerService.addOptionById(
                email,
                ticketId,
                ticketOptionCreateReqDto.getName(),
                ticketOptionCreateReqDto.getPrice()
        );

        return new ResponseEntity<>(ticketOptionDetailResDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{ticketId}/options/{optionId}")
    public ResponseEntity<Void> deleteOptionById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @PathVariable Long ticketId,
                                                 @PathVariable Long optionId) {
        String email = userDetails.getUsername();
        ticketPartnerService.deleteOptionById(email, ticketId, optionId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{ticketId}/options/{optionId}")
    public ResponseEntity<TicketOptionDetailResDto> updateOptionById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                     @PathVariable Long ticketId,
                                                                     @PathVariable Long optionId,
                                                                     @RequestBody TicketOptionUpdateReqDto ticketOptionUpdateReqDto) {
        String email = userDetails.getUsername();
        TicketOptionDetailResDto ticketOptionDetailResDto = ticketPartnerService.updateOptionById(
                email,
                ticketId,
                optionId,
                ticketOptionUpdateReqDto.getName(),
                ticketOptionUpdateReqDto.getPrice()
        );

        return new ResponseEntity<>(ticketOptionDetailResDto, HttpStatus.OK);
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

    @DeleteMapping("/{ticketId}/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteScheduleById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @PathVariable Long ticketId,
                                                   @PathVariable Long scheduleId) {
        String email = userDetails.getUsername();
        ticketPartnerService.deleteScheduleById(email, ticketId, scheduleId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{ticketId}/schedules/{scheduleId}")
    public ResponseEntity<TicketScheduleDetailResDto> updateScheduleById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                         @PathVariable Long ticketId,
                                                                         @PathVariable Long scheduleId,
                                                                         @Valid @RequestBody TicketScheduleUpdateReqDto ticketScheduleUpdateReqDto) {
        String email = userDetails.getUsername();
        TicketScheduleDetailResDto ticketScheduleDetailResDto = ticketPartnerService.updateScheduleById(
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

    @PostMapping("/{ticketId}/images")
    public ResponseEntity<TicketImageDetailsResDto> addImageById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @PathVariable Long ticketId,
                                                                 @RequestParam("images") List<MultipartFile> files) {
        String email = userDetails.getUsername();
        TicketImageDetailsResDto ticketImageDetailsResDto = ticketPartnerService.addImageById(email, ticketId, files);

        return new ResponseEntity<>(ticketImageDetailsResDto, HttpStatus.CREATED);
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
