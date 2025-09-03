package jiyeon.travel.domain.ticket.dto;

import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class TicketScheduleDto {

    private final Long scheduleId;
    private final boolean isActive;
    private final LocalDate startDate;
    private final LocalTime startTime;
    private final int quantity;
    private final int remainingQuantity;

    public static TicketScheduleDto from(TicketSchedule ticketSchedule) {
        return new TicketScheduleDto(
                ticketSchedule.getId(),
                ticketSchedule.isActive(),
                ticketSchedule.getStartDate(),
                ticketSchedule.getStartTime(),
                ticketSchedule.getQuantity(),
                ticketSchedule.getRemainingQuantity()
        );
    }
}
