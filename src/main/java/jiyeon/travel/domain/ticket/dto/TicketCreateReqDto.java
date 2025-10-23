package jiyeon.travel.domain.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "티켓 등록 요청")
public class TicketCreateReqDto {

    @Schema(description = "티켓명")
    @NotBlank(message = "티켓명은 필수입니다.")
    private final String name;

    @Schema(description = "판매 시작일")
    @NotNull(message = "판매 시작일은 필수입니다.")
    private final LocalDateTime saleStartDate;

    @Schema(description = "판매 종료일")
    @NotNull(message = "판매 종료일은 필수입니다.")
    private final LocalDateTime saleEndDate;

    @Schema(description = "전화번호", example = "01012349876")
    @NotBlank(message = "전화번호는 필수입니다.")
    private final String phone;

    @Schema(description = "주소")
    @NotBlank(message = "주소는 필수입니다.")
    private final String address;

    @Schema(description = "옵션이 없을 경우 티켓의 기본 가격", example = "10000")
    private final Integer basePrice;

    @Schema(description = "설명")
    private final String description;

    @Schema(description = "티켓 옵션 목록")
    @Valid
    private final List<TicketOptionCreateReqDto> options;

    @Schema(description = "티켓 일정 목록")
    @NotEmpty(message = "일정은 필수입니다.")
    private final List<TicketScheduleCreateReqDto> schedules;
}
