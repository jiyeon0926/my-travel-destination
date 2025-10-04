package jiyeon.travel.domain.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Schema(description = "티켓 정보 수정 요청")
public class TicketInfoUpdateReqDto {

    @Schema(description = "티켓명")
    private final String name;

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
}
