package jiyeon.travel.domain.ticket.dto;

import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketImage;
import jiyeon.travel.domain.ticket.entity.TicketOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class TicketDetailWithBlogResDto {

    private final Long id;
    private final String ticketName;
    private final LocalDateTime saleStartDate;
    private final LocalDateTime saleEndDate;
    private final Integer basePrice;
    private final String phone;
    private final String address;
    private final String description;
    private final String saleStatus;
    private final List<TicketOptionDto> options;
    private final List<TicketImageDto> images;
    private final List<TicketBlogDto> blogs;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public TicketDetailWithBlogResDto(Ticket ticket, List<TicketOption> options, List<TicketImage> images, List<TicketBlogDto> blogs) {
        this.id = ticket.getId();
        this.ticketName = ticket.getName();
        this.saleStartDate = ticket.getSaleStartDate();
        this.saleEndDate = ticket.getSaleEndDate();
        this.basePrice = ticket.getBasePrice();
        this.phone = ticket.getPhone();
        this.address = ticket.getAddress();
        this.description = ticket.getDescription();
        this.saleStatus = ticket.getSaleStatus().name();
        this.options = options.stream().map(TicketOptionDto::from).toList();
        this.blogs = blogs;
        this.images = images.stream().map(TicketImageDto::from).toList();
        this.createdAt = ticket.getCreatedAt();
        this.updatedAt = ticket.getUpdatedAt();
    }
}
