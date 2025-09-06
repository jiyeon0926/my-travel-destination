package jiyeon.travel.domain.ticket.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TicketOptionUpdateReqDto {

    private final String name;
    private final Integer price;
}
