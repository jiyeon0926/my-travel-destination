package jiyeon.travel.domain.ticket.controller;

import jakarta.validation.Valid;
import jiyeon.travel.domain.ticket.dto.*;
import jiyeon.travel.domain.ticket.service.TicketPartnerCommandService;
import jiyeon.travel.domain.ticket.service.TicketPartnerQueryService;
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

    private final TicketPartnerCommandService ticketPartnerCommandService;
    private final TicketPartnerQueryService ticketPartnerQueryService;

    @PostMapping
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
    public ResponseEntity<TicketImageDetailsResDto> addImageById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @PathVariable Long ticketId,
                                                                 @RequestParam("images") List<MultipartFile> files) {
        String email = userDetails.getUsername();
        TicketImageDetailsResDto ticketImageDetailsResDto = ticketPartnerCommandService.addImageById(email, ticketId, files);

        return new ResponseEntity<>(ticketImageDetailsResDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{ticketId}")
    public ResponseEntity<TicketInfoDetailResDto> updateTicketInfoById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                       @PathVariable Long ticketId,
                                                                       @RequestBody TicketInfoUpdateReqDto ticketInfoUpdateReqDto) {
        String email = userDetails.getUsername();
        TicketInfoDetailResDto ticketInfoResDto = ticketPartnerCommandService.updateTicketInfoById(
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
    public ResponseEntity<TicketInfoDetailResDto> changeTicketStatusById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                         @PathVariable Long ticketId,
                                                                         @Valid @RequestBody TicketStatusReqDto ticketStatusReqDto) {
        String email = userDetails.getUsername();
        TicketInfoDetailResDto ticketInfoDetailResDto = ticketPartnerCommandService.changeTicketStatusById(email, ticketId, ticketStatusReqDto.getSaleStatus());

        return new ResponseEntity<>(ticketInfoDetailResDto, HttpStatus.OK);
    }

    @PatchMapping("/{ticketId}/options/{optionId}")
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
    public ResponseEntity<TicketImageDetailResDto> changeImageMainById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                       @PathVariable Long ticketId,
                                                                       @PathVariable Long imageId) {
        String email = userDetails.getUsername();
        TicketImageDetailResDto ticketImageDetailResDto = ticketPartnerCommandService.changeImageMainById(email, ticketId, imageId);

        return new ResponseEntity<>(ticketImageDetailResDto, HttpStatus.OK);
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> deleteTicketById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @PathVariable Long ticketId) {
        String email = userDetails.getUsername();
        ticketPartnerCommandService.deleteTicketById(ticketId, email);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{ticketId}/options/{optionId}")
    public ResponseEntity<Void> deleteOptionById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @PathVariable Long ticketId,
                                                 @PathVariable Long optionId) {
        String email = userDetails.getUsername();
        ticketPartnerCommandService.deleteOptionById(email, ticketId, optionId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{ticketId}/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteScheduleById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @PathVariable Long ticketId,
                                                   @PathVariable Long scheduleId) {
        String email = userDetails.getUsername();
        ticketPartnerCommandService.deleteScheduleById(email, ticketId, scheduleId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{ticketId}/images/{imageId}")
    public ResponseEntity<Void> deleteImageById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @PathVariable Long ticketId,
                                                @PathVariable Long imageId) {
        String email = userDetails.getUsername();
        ticketPartnerCommandService.deleteImageById(email, ticketId, imageId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketDetailResDto> findMyTicketById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @PathVariable Long ticketId) {
        String email = userDetails.getUsername();
        TicketDetailResDto ticketDetailResDto = ticketPartnerQueryService.findMyTicketById(email, ticketId);

        return new ResponseEntity<>(ticketDetailResDto, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<TicketListResDto> searchMyTickets(@RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(required = false) String saleStatus,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUsername();
        TicketListResDto ticketListResDto = ticketPartnerQueryService.searchMyTickets(page, size, saleStatus, email);

        return new ResponseEntity<>(ticketListResDto, HttpStatus.OK);
    }
}
