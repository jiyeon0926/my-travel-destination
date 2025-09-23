package jiyeon.travel.domain.blog.controller;

import jakarta.validation.Valid;
import jiyeon.travel.domain.blog.dto.*;
import jiyeon.travel.domain.blog.service.BlogService;
import jiyeon.travel.global.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @PostMapping
    public ResponseEntity<BlogDetailResDto> createBlog(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @Valid @RequestPart("blog") BlogCreateReqDto blogCreateReqDto,
                                                       @RequestPart(value = "images", required = false) List<MultipartFile> files) {
        String email = userDetails.getUsername();
        BlogDetailResDto blogDetailResDto = blogService.createBlog(
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

    @GetMapping("/me")
    public ResponseEntity<MyBlogListResDto> findAllMyBlogs(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUsername();
        MyBlogListResDto myBlogListResDto = blogService.findAllMyBlogs(email);

        return new ResponseEntity<>(myBlogListResDto, HttpStatus.OK);
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<BlogDetailResDto> findBlogById(@PathVariable Long blogId) {
        BlogDetailResDto blogDetailResDto = blogService.findBlogById(blogId);

        return new ResponseEntity<>(blogDetailResDto, HttpStatus.OK);
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<Void> deleteBlogById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @PathVariable Long blogId) {
        String email = userDetails.getUsername();
        blogService.deleteBlogById(email, blogId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{blogId}")
    public ResponseEntity<BlogSimpleResDto> updateBlogById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @PathVariable Long blogId,
                                                           @RequestBody BlogUpdateReqDto blogUpdateReqDto) {
        String email = userDetails.getUsername();
        BlogSimpleResDto blogSimpleResDto = blogService.updateBlogById(
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

    @PostMapping("/{blogId}/images")
    public ResponseEntity<BlogImageDetailsResDto> addImageById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @PathVariable Long blogId,
                                                               @RequestParam("images") List<MultipartFile> files) {
        String email = userDetails.getUsername();
        BlogImageDetailsResDto blogImageDetailsResDto = blogService.addImageById(email, blogId, files);

        return new ResponseEntity<>(blogImageDetailsResDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{blogId}/images/{imageId}")
    public ResponseEntity<Void> deleteImageById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @PathVariable Long blogId,
                                                @PathVariable Long imageId) {
        String email = userDetails.getUsername();
        blogService.deleteImageById(email, blogId, imageId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{blogId}/images/{imageId}/main")
    public ResponseEntity<BlogImageDetailResDto> changeImageMainById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                     @PathVariable Long blogId,
                                                                     @PathVariable Long imageId) {
        String email = userDetails.getUsername();
        BlogImageDetailResDto blogImageDetailResDto = blogService.changeImageMainById(email, blogId, imageId);

        return new ResponseEntity<>(blogImageDetailResDto, HttpStatus.OK);
    }

    @PostMapping("/{blogId}/ticket-items")
    public ResponseEntity<BlogTicketItemDetailsResDto> addTicketItemById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                         @PathVariable Long blogId,
                                                                         @Valid @RequestBody BlogTicketItemReqDto blogTicketItemReqDto) {
        String email = userDetails.getUsername();
        BlogTicketItemDetailsResDto blogTicketItemDetailsResDto = blogService.addTicketItemById(
                email,
                blogId,
                blogTicketItemReqDto.getReservationId()
        );

        return new ResponseEntity<>(blogTicketItemDetailsResDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{blogId}/ticket-items/{itemId}")
    public ResponseEntity<Void> deleteTicketItemById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @PathVariable Long blogId,
                                                     @PathVariable Long itemId) {
        String email = userDetails.getUsername();
        blogService.deleteTicketItemById(email, blogId, itemId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
