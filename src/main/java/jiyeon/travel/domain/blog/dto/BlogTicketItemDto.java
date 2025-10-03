package jiyeon.travel.domain.blog.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Schema(description = "티켓 사용 내역 정보")
public class BlogTicketItemDto {

    @Schema(description = "티켓 사용 내역 고유 식별자", example = "1")
    private final Long itemId;

    @Schema(description = "예약 고유 식별자", example = "1")
    private final Long reservationId;

    @Schema(description = "티켓명")
    private final String ticketName;

    @Schema(description = "일정 시작일자", example = "2025-09-12")
    private final LocalDate startDate;

    @Schema(description = "일정 시작시간", example = "11:00:00")
    private final LocalTime startTime;

    @Schema(description = "주소")
    private final String address;

    @Schema(description = "총 수량", example = "1")
    private final int totalQuantity;

    @Schema(description = "총 금액", example = "20000")
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
