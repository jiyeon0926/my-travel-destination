package jiyeon.travel.domain.ticket.dto;

import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketImage;
import jiyeon.travel.domain.ticket.entity.TicketOption;
import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class TicketDetailResDto {

    private final Long id;
    private final Long userId;
    private final String ticketName;
    private final LocalDateTime saleStartDate;
    private final LocalDateTime saleEndDate;
    private final Integer basePrice;
    private final String phone;
    private final String address;
    private final String description;
    private final String status;
    private final List<TicketOptionResDto> options;
    private final List<TicketScheduleResDto> schedules;
    private final List<TicketImageResDto> images;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public TicketDetailResDto(Ticket ticket, List<TicketOption> options, List<TicketSchedule> schedules, List<TicketImage> images) {
        this.id = ticket.getId();
        this.userId = ticket.getUser().getId();
        this.ticketName = ticket.getName();
        this.saleStartDate = ticket.getSaleStartDate();
        this.saleEndDate = ticket.getSaleEndDate();
        this.basePrice = ticket.getBasePrice();
        this.phone = ticket.getPhone();
        this.address = ticket.getAddress();
        this.description = ticket.getDescription();
        this.status = ticket.getStatus().name();
        this.options = options.stream().map(TicketOptionResDto::from).toList();
        this.schedules = schedules.stream().map(TicketScheduleResDto::from).toList();
        this.images = images.stream().map(TicketImageResDto::from).toList();
        this.createdAt = ticket.getCreatedAt();
        this.updatedAt = ticket.getUpdatedAt();
    }
}
