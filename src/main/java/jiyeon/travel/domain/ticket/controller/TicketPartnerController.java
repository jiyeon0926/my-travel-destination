package jiyeon.travel.domain.ticket.controller;

import jakarta.validation.Valid;
import jiyeon.travel.domain.ticket.dto.TicketCreateReqDto;
import jiyeon.travel.domain.ticket.dto.TicketDetailResDto;
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
}
