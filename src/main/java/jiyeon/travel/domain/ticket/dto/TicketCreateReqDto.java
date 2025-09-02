package jiyeon.travel.domain.ticket.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class TicketCreateReqDto {

    @NotBlank(message = "티켓명은 필수입니다.")
    private final String name;

    @NotNull(message = "판매 시작일은 필수입니다.")
    private final LocalDateTime saleStartDate;

    @NotNull(message = "판매 종료일은 필수입니다.")
    private final LocalDateTime saleEndDate;

    @NotBlank(message = "전화번호는 필수입니다.")
    private final String phone;

    @NotBlank(message = "주소는 필수입니다.")
    private final String address;

    private final Integer basePrice;
    private final String description;

    @Valid
    private final List<TicketOptionCreateReqDto> options;

    @Valid
    private final List<TicketScheduleCreateReqDto> schedules;
}
