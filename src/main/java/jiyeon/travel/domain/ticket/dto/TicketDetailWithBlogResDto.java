package jiyeon.travel.domain.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketImage;
import jiyeon.travel.domain.ticket.entity.TicketOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "티켓 상세 및 관련 블로그 목록 응답")
public class TicketDetailWithBlogResDto {

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

    @Schema(description = "티켓 판매 상태", example = "ACTIVE")
    private final String saleStatus;

    @Schema(description = "티켓 옵션 목록")
    private final List<TicketOptionDto> options;

    @Schema(description = "티켓 이미지 목록")
    private final List<TicketImageDto> images;

    @Schema(description = "블로그 목록")
    private final List<TicketBlogDto> blogs;

    @Schema(description = "생성일자")
    private final LocalDateTime createdAt;

    @Schema(description = "수정일자")
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
