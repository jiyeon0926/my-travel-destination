package jiyeon.travel.domain.blog.repository.custom;

import jiyeon.travel.domain.blog.dto.BlogListResDto;
import jiyeon.travel.domain.ticket.dto.TicketBlogDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CustomBlogRepository {

    BlogListResDto findAllMyBlogs(String email);

    BlogListResDto searchBlogs(Pageable pageable, String title, LocalDate travelStartDate, LocalDate travelEndDate, Integer totalExpense);

    List<TicketBlogDto> findAllByTicketId(Pageable pageable, Long ticketId);
}
