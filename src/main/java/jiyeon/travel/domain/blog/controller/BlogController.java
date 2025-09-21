package jiyeon.travel.domain.blog.controller;

import jakarta.validation.Valid;
import jiyeon.travel.domain.blog.dto.BlogCreateReqDto;
import jiyeon.travel.domain.blog.dto.BlogDetailResDto;
import jiyeon.travel.domain.blog.dto.BlogImageDetailsResDto;
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

    @PostMapping("/{blogId}/images")
    public ResponseEntity<BlogImageDetailsResDto> addImageById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @PathVariable Long blogId,
                                                               @RequestParam("images") List<MultipartFile> files) {
        String email = userDetails.getUsername();
        BlogImageDetailsResDto blogImageDetailsResDto = blogService.addImageById(email, blogId, files);

        return new ResponseEntity<>(blogImageDetailsResDto, HttpStatus.CREATED);
    }
}
