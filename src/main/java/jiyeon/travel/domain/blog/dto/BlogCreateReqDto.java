package jiyeon.travel.domain.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "블로그 작성 요청")
public class BlogCreateReqDto {

    @Schema(description = "제목")
    @NotBlank(message = "제목은 필수입니다.")
    @Max(value = 50, message = "제목은 최대 50자 입니다.")
    private final String title;

    @Schema(description = "내용")
    @NotBlank(message = "내용은 필수입니다.")
    private final String content;

    @Schema(description = "여행 시작일", example = "2025-09-12")
    @NotNull(message = "여행 시작일은 필수입니다.")
    private final LocalDate travelStartDate;

    @Schema(description = "여행 종료일", example = "2025-09-15")
    @NotNull(message = "여행 종료일은 필수입니다.")
    private final LocalDate travelEndDate;

    @Schema(description = "예상 경비", example = "500000")
    private final int estimatedExpense;

    @Schema(description = "총 경비", example = "540000")
    @NotNull(message = "총 경비는 필수입니다.")
    private final Integer totalExpense;

    @Schema(description = "티켓 사용 내역 등록 목록")
    @Valid
    private final List<BlogTicketItemReqDto> items;
}
