package jiyeon.travel.domain.blog.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BlogTicketItemReqDto {

    @NotNull(message = "예약 아이디는 필수입니다.")
    private final Long reservationId;
}
