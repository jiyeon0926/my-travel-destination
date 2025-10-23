package jiyeon.travel.domain.blog.repository.custom;

import jiyeon.travel.domain.blog.dto.BlogTicketItemDto;
import jiyeon.travel.domain.blog.entity.BlogTicketItem;

import java.util.List;
import java.util.Optional;

public interface CustomBlogTicketItemRepository {

    Optional<BlogTicketItem> findByIdAndBlogIdAndEmail(Long id, Long blogId, String email);

    List<BlogTicketItemDto> findDetailsByBlogId(Long blogId);
}
