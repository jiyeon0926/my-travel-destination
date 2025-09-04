package jiyeon.travel.domain.ticket.dto;

import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class TicketScheduleDetailResDto {

    private final Long ticketId;
    private final Long scheduleId;
    private final boolean isActive;
    private final LocalDate startDate;
    private final LocalTime startTime;
    private final int quantity;
    private final int remainingQuantity;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public TicketScheduleDetailResDto(Ticket ticket, TicketSchedule ticketSchedule) {
        this.ticketId = ticket.getId();
        this.scheduleId = ticketSchedule.getId();
        this.isActive = ticketSchedule.isActive();
        this.startDate = ticketSchedule.getStartDate();
        this.startTime = ticketSchedule.getStartTime();
        this.quantity = ticketSchedule.getQuantity();
        this.remainingQuantity = ticketSchedule.getRemainingQuantity();
        this.createdAt = ticketSchedule.getCreatedAt();
        this.updatedAt = ticketSchedule.getUpdatedAt();
    }
}
