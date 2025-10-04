package jiyeon.travel.domain.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jiyeon.travel.domain.ticket.entity.Ticket;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Schema(description = "티켓 기본 응답")
public class TicketSimpleResDto {

    @Schema(description = "티켓 고유 식별자", example = "1")
    private final Long id;

    @Schema(description = "티켓명")
    private final String ticketName;

    @Schema(description = "판매 시작일")
    private final LocalDateTime saleStartDate;

    @Schema(description = "판매 종료일")
    private final LocalDateTime saleEndDate;

    @Schema(description = "옵션이 없을 경우 티켓의 기본 가격", example = "10000")
    private final Integer basePrice;

    @Schema(description = "전화번호", example = "01012349876")
    private final String phone;

    @Schema(description = "주소")
    private final String address;

    @Schema(description = "설명")
    private final String description;

    @Schema(description = "티켓 판매 상태", example = "READY")
    private final String saleStatus;

    @Schema(description = "생성일자")
    private final LocalDateTime createdAt;

    @Schema(description = "수정일자")
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
