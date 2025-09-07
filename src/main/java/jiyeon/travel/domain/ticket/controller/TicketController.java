package jiyeon.travel.domain.ticket.controller;

import jiyeon.travel.domain.ticket.dto.TicketOptionDetailsResDto;
import jiyeon.travel.domain.ticket.dto.TicketScheduleDetailsResDto;
import jiyeon.travel.domain.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/{ticketId}/options")
    public ResponseEntity<TicketOptionDetailsResDto> getBaseOrOptionById(@PathVariable Long ticketId) {
        TicketOptionDetailsResDto ticketOptionDetailsResDto = ticketService.getBaseOrOptionById(ticketId);

        return new ResponseEntity<>(ticketOptionDetailsResDto, HttpStatus.OK);
    }

    @GetMapping("/{ticketId}/schedules")
    public ResponseEntity<TicketScheduleDetailsResDto> getScheduleById(@PathVariable Long ticketId) {
        TicketScheduleDetailsResDto ticketScheduleDetailsResDto = ticketService.getScheduleById(ticketId);

        return new ResponseEntity<>(ticketScheduleDetailsResDto, HttpStatus.OK);
    }
}
