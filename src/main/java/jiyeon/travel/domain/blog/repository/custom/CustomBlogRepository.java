package jiyeon.travel.domain.blog.repository.custom;

import jiyeon.travel.domain.blog.dto.BlogListResDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface CustomBlogRepository {

    BlogListResDto findAllMyBlogs(String email);

    BlogListResDto searchBlogs(Pageable pageable, String title, LocalDate travelStartDate, LocalDate travelEndDate, Integer totalExpense);
}
