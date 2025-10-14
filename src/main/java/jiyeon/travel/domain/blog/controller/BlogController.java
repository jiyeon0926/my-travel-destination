package jiyeon.travel.domain.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jiyeon.travel.domain.blog.dto.*;
import jiyeon.travel.domain.blog.service.BlogCommandService;
import jiyeon.travel.domain.blog.service.BlogQueryService;
import jiyeon.travel.global.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
@Tag(
        name = "Blog",
        description = "사용자가 블로그를 작성, 수정, 삭제, 조회 및 검색할 수 있는 기능을 제공합니다."
)
public class BlogController {

    private final BlogCommandService blogCommandService;
    private final BlogQueryService blogQueryService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "블로그 작성", description = "사용자가 제목, 여행 기간, 예상 경비, 총 경비 및 내용을 작성하며, 티켓 사용 내역을 추가하거나 이미지를 업로드할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlogDetailResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "사용자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json"))
    })
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
    @Operation(summary = "블로그 이미지 추가", description = "사용자는 여러 이미지를 추가할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlogImageDetailsResDto.class))),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "사용자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "블로그를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<BlogImageDetailsResDto> addImageById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @PathVariable Long blogId,
                                                               @RequestParam("images") List<MultipartFile> files) {
        String email = userDetails.getUsername();
        BlogImageDetailsResDto blogImageDetailsResDto = blogCommandService.addImageById(email, blogId, files);

        return new ResponseEntity<>(blogImageDetailsResDto, HttpStatus.CREATED);
    }

    @PostMapping("/{blogId}/ticket-items")
    @Operation(summary = "블로그 티켓 사용 내역 추가", description = "사용자가 예약한 티켓 중 사용 완료된 티켓을 블로그에 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlogTicketItemDetailsResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "사용자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "블로그 또는 예약 아이디를 찾을 수 없습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "이미 블로그에 동일한 티켓 사용 내역이 존재합니다.", content = @Content(mediaType = "application/json"))
    })
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
    @Operation(summary = "블로그 수정", description = "사용자는 제목, 여행 기간, 예상 경비, 총 경비, 내용을 수정할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlogSimpleResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "사용자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "블로그를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<BlogSimpleResDto> updateBlogById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @PathVariable Long blogId,
                                                           @Valid @RequestBody BlogUpdateReqDto blogUpdateReqDto) {
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
    @Operation(summary = "블로그 대표 이미지 변경", description = "업로드된 이미지 중 하나를 선택하여 블로그 대표 이미지로 설정합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlogImageDetailResDto.class))),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "사용자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "블로그 이미지를 찾을 수 없습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "이미 대표 이미지입니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<BlogImageDetailResDto> changeImageMainById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                     @PathVariable Long blogId,
                                                                     @PathVariable Long imageId) {
        String email = userDetails.getUsername();
        BlogImageDetailResDto blogImageDetailResDto = blogCommandService.changeImageMainById(email, blogId, imageId);

        return new ResponseEntity<>(blogImageDetailResDto, HttpStatus.OK);
    }

    @DeleteMapping("/{blogId}")
    @Operation(summary = "블로그 삭제", description = "블로그를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "사용자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "블로그를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> deleteBlogById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @PathVariable Long blogId) {
        String email = userDetails.getUsername();
        blogCommandService.deleteBlogById(email, blogId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{blogId}/images/{imageId}")
    @Operation(summary = "블로그 이미지 삭제", description = "블로그에 업로드된 이미지를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "사용자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "블로그 이미지를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> deleteImageById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @PathVariable Long blogId,
                                                @PathVariable Long imageId) {
        String email = userDetails.getUsername();
        blogCommandService.deleteImageById(email, blogId, imageId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{blogId}/ticket-items/{itemId}")
    @Operation(summary = "블로그 티켓 사용 내역 삭제", description = "블로그에 등록된 티켓 사용 내역을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "사용자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "블로그 티켓 사용 내역을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> deleteTicketItemById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @PathVariable Long blogId,
                                                     @PathVariable Long itemId) {
        String email = userDetails.getUsername();
        blogCommandService.deleteTicketItemById(email, blogId, itemId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{blogId}")
    @Operation(summary = "블로그 상세 조회", description = "블로그의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlogDetailResDto.class))),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "사용자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "블로그를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<BlogDetailResDto> findBlogById(@PathVariable Long blogId) {
        BlogDetailResDto blogDetailResDto = blogQueryService.findBlogById(blogId);

        return new ResponseEntity<>(blogDetailResDto, HttpStatus.OK);
    }

    @GetMapping("/me")
    @Operation(summary = "내가 작성한 블로그 전체 조회", description = "사용자가 작성한 모든 블로그를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlogListResDto.class))),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "사용자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<BlogListResDto> findAllMyBlogs(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUsername();
        BlogListResDto blogListResDto = blogQueryService.findAllMyBlogs(email);

        return new ResponseEntity<>(blogListResDto, HttpStatus.OK);
    }

    @GetMapping("/search")
    @Operation(summary = "블로그 기반 여행지 검색", description = "제목, 여행 기간, 총 경비를 기준으로 블로그를 기반으로 한 여행지를 검색할 수 있습니다.")
    @Parameters(value = {
            @Parameter(name = "title", description = "제목"),
            @Parameter(name = "travelStartDate", description = "여행 시작일", example = "2025-09-12"),
            @Parameter(name = "travelEndDate", description = "여행 종료일", example = "2025-09-15"),
            @Parameter(name = "totalExpense", description = "총 경비", example = "540000")
    })
    @ApiResponse(
            responseCode = "200", description = "요청에 성공하였습니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlogListResDto.class))
    )
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
