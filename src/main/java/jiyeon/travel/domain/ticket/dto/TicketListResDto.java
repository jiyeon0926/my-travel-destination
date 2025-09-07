package jiyeon.travel.domain.ticket.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class TicketListResDto {

    private final Long total;
    private final List<TicketSimpleResDto> tickets;
}
