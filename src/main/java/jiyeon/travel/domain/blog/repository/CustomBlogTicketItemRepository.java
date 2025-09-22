package jiyeon.travel.domain.blog.repository;

import jiyeon.travel.domain.blog.dto.BlogTicketItemDto;

import java.util.List;

public interface CustomBlogTicketItemRepository {

    List<BlogTicketItemDto> findDetailsByBlogId(Long blogId);
}
