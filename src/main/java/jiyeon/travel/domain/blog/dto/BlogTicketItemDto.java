package jiyeon.travel.domain.blog.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class BlogTicketItemDto {

    private final Long itemId;
    private final Long reservationId;
    private final String ticketName;
    private final LocalDate startDate;
    private final LocalTime startTime;
    private final String address;
    private final int totalQuantity;
    private final int totalAmount;

    @QueryProjection
    public BlogTicketItemDto(Long itemId, Long reservationId, String ticketName, LocalDate startDate, LocalTime startTime, String address, int totalQuantity, int totalAmount) {
        this.itemId = itemId;
        this.reservationId = reservationId;
        this.ticketName = ticketName;
        this.startDate = startDate;
        this.startTime = startTime;
        this.address = address;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
    }
}
