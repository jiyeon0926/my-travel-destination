package jiyeon.travel.domain.ticket.dto;

import jiyeon.travel.domain.ticket.entity.Ticket;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class TicketSimpleResDto {

    private final Long id;
    private final String ticketName;
    private final LocalDateTime saleStartDate;
    private final LocalDateTime saleEndDate;
    private final Integer basePrice;
    private final String phone;
    private final String address;
    private final String description;
    private final String saleStatus;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public TicketSimpleResDto(Ticket ticket) {
        this.id = ticket.getId();
        this.ticketName = ticket.getName();
        this.saleStartDate = ticket.getSaleStartDate();
        this.saleEndDate = ticket.getSaleEndDate();
        this.basePrice = ticket.getBasePrice();
        this.phone = ticket.getPhone();
        this.address = ticket.getAddress();
        this.description = ticket.getDescription();
        this.saleStatus = ticket.getSaleStatus().name();
        this.createdAt = ticket.getCreatedAt();
        this.updatedAt = ticket.getUpdatedAt();
    }
}
