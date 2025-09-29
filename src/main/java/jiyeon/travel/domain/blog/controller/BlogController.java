package jiyeon.travel.domain.blog.controller;

import jakarta.validation.Valid;
import jiyeon.travel.domain.blog.dto.*;
import jiyeon.travel.domain.blog.service.BlogCommandService;
import jiyeon.travel.domain.blog.service.BlogQueryService;
import jiyeon.travel.global.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogCommandService blogCommandService;
    private final BlogQueryService blogQueryService;

    @PostMapping
    public ResponseEntity<BlogDetailResDto> createBlog(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @Valid @RequestPart("blog") BlogCreateReqDto blogCreateReqDto,
                                                       @RequestPart(value = "images", required = false) List<MultipartFile> files) {
        String email = userDetails.getUsername();
        BlogDetailResDto blogDetailResDto = blogCommandService.createBlog(
                email,
                blogCreateReqDto.getTitle(),
                blogCreateReqDto.getContent(),
                blogCreateReqDto.getTravelStartDate(),
                blogCreateReqDto.getTravelEndDate(),
                blogCreateReqDto.getEstimatedExpense(),
                blogCreateReqDto.getTotalExpense(),
                blogCreateReqDto.getItems(),
                files
        );

        return new ResponseEntity<>(blogDetailResDto, HttpStatus.CREATED);
    }

    @PostMapping("/{blogId}/images")
    public ResponseEntity<BlogImageDetailsResDto> addImageById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @PathVariable Long blogId,
                                                               @RequestParam("images") List<MultipartFile> files) {
        String email = userDetails.getUsername();
        BlogImageDetailsResDto blogImageDetailsResDto = blogCommandService.addImageById(email, blogId, files);

        return new ResponseEntity<>(blogImageDetailsResDto, HttpStatus.CREATED);
    }

    @PostMapping("/{blogId}/ticket-items")
    public ResponseEntity<BlogTicketItemDetailsResDto> addTicketItemById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                         @PathVariable Long blogId,
                                                                         @Valid @RequestBody BlogTicketItemReqDto blogTicketItemReqDto) {
        String email = userDetails.getUsername();
        BlogTicketItemDetailsResDto blogTicketItemDetailsResDto = blogCommandService.addTicketItemById(
                email,
                blogId,
                blogTicketItemReqDto.getReservationId()
        );

        return new ResponseEntity<>(blogTicketItemDetailsResDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{blogId}")
    public ResponseEntity<BlogSimpleResDto> updateBlogById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @PathVariable Long blogId,
                                                           @RequestBody BlogUpdateReqDto blogUpdateReqDto) {
        String email = userDetails.getUsername();
        BlogSimpleResDto blogSimpleResDto = blogCommandService.updateBlogById(
                email,
                blogId,
                blogUpdateReqDto.getTitle(),
                blogUpdateReqDto.getContent(),
                blogUpdateReqDto.getTravelStartDate(),
                blogUpdateReqDto.getTravelEndDate(),
                blogUpdateReqDto.getEstimatedExpense(),
                blogUpdateReqDto.getTotalExpense()
        );

        return new ResponseEntity<>(blogSimpleResDto, HttpStatus.OK);
    }

    @PatchMapping("/{blogId}/images/{imageId}/main")
    public ResponseEntity<BlogImageDetailResDto> changeImageMainById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                     @PathVariable Long blogId,
                                                                     @PathVariable Long imageId) {
        String email = userDetails.getUsername();
        BlogImageDetailResDto blogImageDetailResDto = blogCommandService.changeImageMainById(email, blogId, imageId);

        return new ResponseEntity<>(blogImageDetailResDto, HttpStatus.OK);
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<Void> deleteBlogById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @PathVariable Long blogId) {
        String email = userDetails.getUsername();
        blogCommandService.deleteBlogById(email, blogId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{blogId}/images/{imageId}")
    public ResponseEntity<Void> deleteImageById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @PathVariable Long blogId,
                                                @PathVariable Long imageId) {
        String email = userDetails.getUsername();
        blogCommandService.deleteImageById(email, blogId, imageId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{blogId}/ticket-items/{itemId}")
    public ResponseEntity<Void> deleteTicketItemById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @PathVariable Long blogId,
                                                     @PathVariable Long itemId) {
        String email = userDetails.getUsername();
        blogCommandService.deleteTicketItemById(email, blogId, itemId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<BlogDetailResDto> findBlogById(@PathVariable Long blogId) {
        BlogDetailResDto blogDetailResDto = blogQueryService.findBlogById(blogId);

        return new ResponseEntity<>(blogDetailResDto, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<BlogListResDto> findAllMyBlogs(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUsername();
        BlogListResDto blogListResDto = blogQueryService.findAllMyBlogs(email);

        return new ResponseEntity<>(blogListResDto, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<BlogListResDto> searchBlogs(@RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(required = false) String title,
                                                      @RequestParam(required = false) LocalDate travelStartDate,
                                                      @RequestParam(required = false) LocalDate travelEndDate,
                                                      @RequestParam(required = false) Integer totalExpense) {
        BlogListResDto blogListResDto = blogQueryService.searchBlogs(page, size, title, travelStartDate, travelEndDate, totalExpense);

        return new ResponseEntity<>(blogListResDto, HttpStatus.OK);
    }
}
