package jiyeon.travel.domain.blog.service;

import jiyeon.travel.domain.blog.dto.BlogDetailResDto;
import jiyeon.travel.domain.blog.dto.BlogListResDto;
import jiyeon.travel.domain.blog.dto.BlogTicketItemDto;
import jiyeon.travel.domain.blog.entity.Blog;
import jiyeon.travel.domain.blog.repository.BlogRepository;
import jiyeon.travel.domain.ticket.dto.TicketBlogDto;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogQueryService {

    private final BlogRepository blogRepository;
    private final BlogTicketItemQueryService blogTicketItemQueryService;

    @Transactional(readOnly = true)
    public BlogDetailResDto findBlogById(Long blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new CustomException(ErrorCode.BLOG_NOT_FOUND));
        List<BlogTicketItemDto> blogTicketItemDtoList = blogTicketItemQueryService.findTicketItemsByBlogId(blogId);

        return new BlogDetailResDto(blog, blog.getBlogImages(), blogTicketItemDtoList);
    }

    @Transactional(readOnly = true)
    public BlogListResDto findAllMyBlogs(String email) {
        return blogRepository.findAllMyBlogs(email);
    }

    @Transactional(readOnly = true)
    public BlogListResDto searchBlogs(int page, int size, String title, LocalDate travelStartDate, LocalDate travelEndDate, Integer totalExpense) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return blogRepository.searchBlogs(pageable, title, travelStartDate, travelEndDate, totalExpense);
    }

    public List<TicketBlogDto> findBlogsByTicketId(int page, int size, Long ticketId) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return blogRepository.findAllByTicketId(pageable, ticketId);
    }
}
