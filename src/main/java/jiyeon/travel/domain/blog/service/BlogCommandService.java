package jiyeon.travel.domain.blog.service;

import jiyeon.travel.domain.blog.dto.*;
import jiyeon.travel.domain.blog.entity.Blog;
import jiyeon.travel.domain.blog.entity.BlogImage;
import jiyeon.travel.domain.blog.repository.BlogRepository;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.domain.user.service.UserQueryService;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class BlogCommandService {

    private final BlogRepository blogRepository;
    private final BlogImageCommandService blogImageCommandService;
    private final BlogTicketItemCommandService blogTicketItemCommandService;
    private final BlogTicketItemQueryService blogTicketItemQueryService;
    private final UserQueryService userQueryService;

    @Transactional
    public BlogDetailResDto createBlog(String email, String title, String content, LocalDate travelStartDate,
                                       LocalDate travelEndDate, int estimatedExpense, Integer totalExpense,
                                       List<BlogTicketItemReqDto> items, List<MultipartFile> files) {
        validateTravelRange(travelStartDate, travelEndDate);

        User user = userQueryService.getActiveUserByEmail(email);
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
        List<BlogImage> savedBlogImages = (files != null) ? blogImageCommandService.saveImages(savedBlog, files) : Collections.emptyList();
        if (items != null) blogTicketItemCommandService.saveTicketItem(email, savedBlog, items);

        List<BlogTicketItemDto> blogTicketItemDtos = blogTicketItemQueryService.findTicketItemsByBlogId(savedBlog.getId());

        return new BlogDetailResDto(savedBlog, savedBlogImages, blogTicketItemDtos);
    }

    @Transactional
    public BlogImageDetailsResDto addImageById(String email, Long blogId, List<MultipartFile> files) {
        Blog blog = blogRepository.findByIdAndEmailOrElseThrow(blogId, email);
        List<BlogImage> blogImages = blogImageCommandService.addImages(blog, files);

        return new BlogImageDetailsResDto(blog, blogImages);
    }

    @Transactional
    public BlogTicketItemDetailsResDto addTicketItemById(String email, Long blogId, Long reservationId) {
        Blog blog = blogRepository.findByIdAndEmailOrElseThrow(blogId, email);
        blogTicketItemCommandService.addTicketItem(email, blog, reservationId);

        List<BlogTicketItemDto> blogTicketItemDtos = blogTicketItemQueryService.findTicketItemsByBlogId(blogId);

        return new BlogTicketItemDetailsResDto(blog, blogTicketItemDtos);
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
    public BlogImageDetailResDto changeImageMainById(String email, Long blogId, Long imageId) {
        BlogImage blogImage = blogImageCommandService.changeImageMain(email, blogId, imageId);

        return new BlogImageDetailResDto(blogImage.getBlog(), blogImage);
    }

    @Transactional
    public void deleteBlogById(String email, Long blogId) {
        Blog blog = blogRepository.findByIdAndEmailOrElseThrow(blogId, email);
        blogImageCommandService.deleteImagesFromS3(blogId);
        blogRepository.delete(blog);
    }

    @Transactional
    public void deleteImageById(String email, Long blogId, Long imageId) {
        blogImageCommandService.deleteImage(email, blogId, imageId);
    }

    @Transactional
    public void deleteTicketItemById(String email, Long blogId, Long itemId) {
        blogTicketItemCommandService.deleteTicketItem(email, blogId, itemId);
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
