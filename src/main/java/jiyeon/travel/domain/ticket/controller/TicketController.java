package jiyeon.travel.domain.ticket.controller;

import jiyeon.travel.domain.ticket.dto.TicketDetailWithBlogResDto;
import jiyeon.travel.domain.ticket.dto.TicketListResDto;
import jiyeon.travel.domain.ticket.dto.TicketOptionDetailsResDto;
import jiyeon.travel.domain.ticket.dto.TicketScheduleDetailsResDto;
import jiyeon.travel.domain.ticket.service.TicketQueryFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketQueryFacade ticketQueryFacade;

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketDetailWithBlogResDto> findTicketById(@PathVariable Long ticketId,
                                                                           @RequestParam(defaultValue = "1") int page,
                                                                           @RequestParam(defaultValue = "10") int size) {
        TicketDetailWithBlogResDto ticketDetailWithBlogResDto = ticketQueryFacade.findTicketById(page, size, ticketId);

        return new ResponseEntity<>(ticketDetailWithBlogResDto, HttpStatus.OK);
    }

    @GetMapping("/{ticketId}/options")
    public ResponseEntity<TicketOptionDetailsResDto> getBaseOrOptionById(@PathVariable Long ticketId) {
        TicketOptionDetailsResDto ticketOptionDetailsResDto = ticketQueryFacade.getBaseOrOptionById(ticketId);

        return new ResponseEntity<>(ticketOptionDetailsResDto, HttpStatus.OK);
    }

    @GetMapping("/{ticketId}/schedules")
    public ResponseEntity<TicketScheduleDetailsResDto> getScheduleById(@PathVariable Long ticketId) {
        TicketScheduleDetailsResDto ticketScheduleDetailsResDto = ticketQueryFacade.getScheduleById(ticketId);

        return new ResponseEntity<>(ticketScheduleDetailsResDto, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<TicketListResDto> searchTickets(@RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(required = false) String name) {
        TicketListResDto ticketListResDto = ticketQueryFacade.searchTickets(page, size, name);

        return new ResponseEntity<>(ticketListResDto, HttpStatus.OK);
    }
}
