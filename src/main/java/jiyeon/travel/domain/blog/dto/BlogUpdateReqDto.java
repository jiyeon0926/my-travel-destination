package jiyeon.travel.domain.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
@Schema(description = "블로그 내용 수정 요청")
public class BlogUpdateReqDto {

    @Schema(description = "제목")
    @Size(max = 50, message = "제목은 최대 50자입니다.")
    private final String title;

    @Schema(description = "내용")
    private final String content;

    @Schema(description = "여행 시작일", example = "2025-09-12")
    private final LocalDate travelStartDate;

    @Schema(description = "여행 종료일", example = "2025-09-15")
    private final LocalDate travelEndDate;

    @Schema(description = "예상 경비", example = "500000")
    private final Integer estimatedExpense;

    @Schema(description = "총 경비", example = "540000")
    private final Integer totalExpense;
}
