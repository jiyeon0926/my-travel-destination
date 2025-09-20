package jiyeon.travel.domain.blog.dto;

import jiyeon.travel.domain.blog.entity.BlogTicketItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BlogTicketItemDto {

    private final Long itemId;
    private final Long reservationId;

    public static BlogTicketItemDto from(BlogTicketItem blogTicketItem) {
        return new BlogTicketItemDto(
                blogTicketItem.getId(),
                blogTicketItem.getReservation().getId()
        );
    }
}
