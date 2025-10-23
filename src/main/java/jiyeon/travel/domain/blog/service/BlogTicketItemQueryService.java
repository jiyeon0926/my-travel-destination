package jiyeon.travel.domain.blog.service;

import jiyeon.travel.domain.blog.dto.BlogTicketItemDto;
import jiyeon.travel.domain.blog.repository.BlogTicketItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogTicketItemQueryService {

    private final BlogTicketItemRepository blogTicketItemRepository;

    public List<BlogTicketItemDto> findTicketItemsByBlogId(Long blogId) {
        return blogTicketItemRepository.findDetailsByBlogId(blogId);
    }
}
