package jiyeon.travel.domain.blog.service;

import jiyeon.travel.domain.blog.dto.*;
import jiyeon.travel.domain.blog.entity.Blog;
import jiyeon.travel.domain.blog.entity.BlogImage;
import jiyeon.travel.domain.blog.repository.BlogRepository;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.domain.user.service.UserService;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

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
        validateTravelRange(travelStartDate, travelEndDate);

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
        if (items != null) blogTicketItemService.saveTicketItem(email, savedBlog, items);

        List<BlogTicketItemDto> blogTicketItemDtos = blogTicketItemService.findTicketItemsByBlogId(savedBlog.getId());

        return new BlogDetailResDto(savedBlog, savedBlogImages, blogTicketItemDtos);
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

    @Transactional(readOnly = true)
    public BlogDetailResDto findBlogById(Long blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new CustomException(ErrorCode.BLOG_NOT_FOUND));
        List<BlogTicketItemDto> blogTicketItemDtos = blogTicketItemService.findTicketItemsByBlogId(blogId);

        return new BlogDetailResDto(blog, blog.getBlogImages(), blogTicketItemDtos);
    }

    @Transactional
    public void deleteBlogById(String email, Long blogId) {
        Blog blog = blogRepository.findByIdAndEmailOrElseThrow(blogId, email);
        blogImageService.deleteImagesFromS3(blogId);
        blogRepository.delete(blog);
    }

    @Transactional
    public BlogSimpleResDto updateBlogById(String email, Long blogId, String title, String content,
                                           LocalDate travelStartDate, LocalDate travelEndDate,
                                           Integer estimatedExpense, Integer totalExpense) {
        Blog blog = blogRepository.findByIdAndEmailOrElseThrow(blogId, email);
        validateTravelRange(travelStartDate, travelEndDate);

        acceptIfNotNull(title, blog::changeTitle);
        acceptIfNotNull(content, blog::changeContent);
        acceptIfNotNull(travelStartDate, blog::changeTravelStartDate);
        acceptIfNotNull(travelEndDate, blog::changeTravelEndDate);
        acceptIfNotNull(estimatedExpense, blog::changeEstimatedExpense);
        acceptIfNotNull(totalExpense, blog::changeTotalExpense);

        return new BlogSimpleResDto(blog);
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

    @Transactional
    public BlogImageDetailResDto changeImageMainById(String email, Long blogId, Long imageId) {
        BlogImage blogImage = blogImageService.changeImageMain(email, blogId, imageId);

        return new BlogImageDetailResDto(blogImage.getBlog(), blogImage);
    }

    @Transactional
    public BlogTicketItemDetailsResDto addTicketItemById(String email, Long blogId, Long reservationId) {
        Blog blog = blogRepository.findByIdAndEmailOrElseThrow(blogId, email);
        blogTicketItemService.addTicketItem(email, blog, reservationId);

        List<BlogTicketItemDto> blogTicketItemDtos = blogTicketItemService.findTicketItemsByBlogId(blogId);

        return new BlogTicketItemDetailsResDto(blog, blogTicketItemDtos);
    }

    @Transactional
    public void deleteTicketItemById(String email, Long blogId, Long itemId) {
        blogTicketItemService.deleteTicketItem(email, blogId, itemId);
    }

    private void validateTravelRange(LocalDate travelStartDate, LocalDate travelEndDate) {
        if (travelStartDate != null && travelEndDate != null) {
            if (travelStartDate.isAfter(travelEndDate)) {
                throw new CustomException(ErrorCode.INVALID_TRAVEL_START_DATE);
            }

            if (travelEndDate.isBefore(travelStartDate)) {
                throw new CustomException(ErrorCode.INVALID_TRAVEL_END_DATE);
            }
        }
    }

    private <T> void acceptIfNotNull(T t, Consumer<T> consumer) {
        if (t != null) consumer.accept(t);
    }
}
