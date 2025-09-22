package jiyeon.travel.domain.blog.service;

import jiyeon.travel.domain.blog.dto.BlogDetailResDto;
import jiyeon.travel.domain.blog.dto.BlogImageDetailsResDto;
import jiyeon.travel.domain.blog.dto.BlogTicketItemReqDto;
import jiyeon.travel.domain.blog.entity.Blog;
import jiyeon.travel.domain.blog.entity.BlogImage;
import jiyeon.travel.domain.blog.entity.BlogTicketItem;
import jiyeon.travel.domain.blog.repository.BlogRepository;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final BlogImageService blogImageService;
    private final BlogTicketItemService blogTicketItemService;
    private final UserService userService;

    @Transactional
    public BlogDetailResDto createBlog(String email, String title, String content, LocalDate travelStartDate,
                                       LocalDate travelEndDate, int estimatedExpense, Integer totalExpense,
                                       List<BlogTicketItemReqDto> items, List<MultipartFile> files) {
        User user = userService.getActiveUserByEmail(email);
        Blog blog = new Blog(
                user,
                title,
                content,
                travelStartDate,
                travelEndDate,
                estimatedExpense,
                totalExpense
        );

        Blog savedBlog = blogRepository.save(blog);
        List<BlogImage> savedBlogImages = (files != null) ? blogImageService.saveImages(savedBlog, files) : Collections.emptyList();
        List<BlogTicketItem> savedBlogTicketItems = (items != null) ? blogTicketItemService.saveTicketItem(email, savedBlog, items) : Collections.emptyList();

        return new BlogDetailResDto(savedBlog, savedBlogImages, savedBlogTicketItems);
    }

    @Transactional
    public BlogImageDetailsResDto addImageById(String email, Long blogId, List<MultipartFile> files) {
        Blog blog = blogRepository.findByIdAndEmailOrElseThrow(blogId, email);
        List<BlogImage> blogImages = blogImageService.addImages(blog, files);

        return new BlogImageDetailsResDto(blog, blogImages);
    }

    @Transactional
    public void deleteImageById(String email, Long blogId, Long imageId) {
        blogImageService.deleteImage(email, blogId, imageId);
    }
}
