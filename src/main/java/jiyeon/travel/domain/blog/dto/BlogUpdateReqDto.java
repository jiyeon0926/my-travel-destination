package jiyeon.travel.domain.blog.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class BlogUpdateReqDto {

    private final String title;
    private final String content;
    private final LocalDate travelStartDate;
    private final LocalDate travelEndDate;
    private final Integer estimatedExpense;
    private final Integer totalExpense;
}
