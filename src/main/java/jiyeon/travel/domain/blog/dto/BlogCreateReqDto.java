package jiyeon.travel.domain.blog.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class BlogCreateReqDto {

    @NotBlank(message = "제목은 필수입니다.")
    private final String title;

    @NotBlank(message = "내용은 필수입니다.")
    private final String content;

    @NotNull(message = "여행 시작일은 필수입니다.")
    private final LocalDate travelStartDate;

    @NotNull(message = "여행 종료일은 필수입니다.")
    private final LocalDate travelEndDate;

    private final int estimatedExpense;

    @NotNull(message = "총 경비는 필수입니다.")
    private final Integer totalExpense;

    @Valid
    private final List<BlogTicketItemReqDto> items;
}
