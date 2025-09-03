package jiyeon.travel.domain.ticket.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class TicketInfoUpdateReqDto {

    private final String name;
    private final LocalDateTime saleStartDate;
    private final LocalDateTime saleEndDate;
    private final Integer basePrice;
    private final String phone;
    private final String address;
    private final String description;
}
