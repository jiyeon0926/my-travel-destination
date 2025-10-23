package jiyeon.travel.domain.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "티켓 목록 응답")
public class TicketListResDto {

    @Schema(description = "합계", example = "5")
    private final Long total;

    @Schema(description = "티켓 목록")
    private final List<TicketPreviewResDto> tickets;
}
