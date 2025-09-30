package jiyeon.travel.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jiyeon.travel.domain.user.dto.UserDetailResDto;
import jiyeon.travel.domain.user.service.UserAdminQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(
        name = "User Management",
        description = "관리자가 사용자를 조회하고 검색할 수 있는 기능을 제공합니다."
)
public class UserAdminController {

    private final UserAdminQueryService userAdminQueryService;

    @GetMapping("/search")
    @Operation(summary = "사용자 검색", description = "관리자는 이메일, 별명, 탈퇴 여부를 기준으로 사용자를 검색할 수 있습니다.")
    @Parameters(value = {
            @Parameter(name = "email", description = "이메일", example = "user1@naver.com"),
            @Parameter(name = "nickname", description = "별명", example = "고양이"),
            @Parameter(name = "isDeleted", description = "탈퇴 여부", example = "true"),
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDetailResDto.class))
                    )),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "관리자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<List<UserDetailResDto>> searchUsers(@RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @RequestParam(required = false) String email,
                                                              @RequestParam(required = false) String nickname,
                                                              @RequestParam(required = false) Boolean isDeleted) {
        List<UserDetailResDto> userDetailResDtoList = userAdminQueryService.searchUsers(page, size, email, nickname, isDeleted);

        return new ResponseEntity<>(userDetailResDtoList, HttpStatus.OK);
    }
}
