package jiyeon.travel.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jiyeon.travel.domain.user.dto.UserProfileResDto;
import jiyeon.travel.domain.user.dto.UserProfileUpdateReqDto;
import jiyeon.travel.domain.user.service.UserCommandService;
import jiyeon.travel.domain.user.service.UserQueryService;
import jiyeon.travel.global.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(
        name = "User",
        description = "사용자가 본인의 프로필을 조회/수정하고, 계정을 탈퇴할 수 있는 기능을 제공합니다."
)
public class UserController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    @PatchMapping("/me")
    @Operation(summary = "사용자 프로필 수정", description = "별명, 전화번호를 수정할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileResDto.class))),
            @ApiResponse(responseCode = "400", description = "이름 또는 전화번호가 비어있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "사용자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<UserProfileResDto> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @RequestBody UserProfileUpdateReqDto userProfileUpdateReqDto) {
        String email = userDetails.getUsername();
        UserProfileResDto userProfileResDto = userCommandService.updateProfile(email, userProfileUpdateReqDto.getNickname(), userProfileUpdateReqDto.getPhone());

        return new ResponseEntity<>(userProfileResDto, HttpStatus.OK);
    }

    @DeleteMapping("/me")
    @Operation(summary = "회원탈퇴", description = "본인의 계정을 탈퇴할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "사용자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUsername();
        userCommandService.deleteUser(email);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/me")
    @Operation(summary = "사용자 프로필 조회", description = "본인의 프로필을 조회할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileResDto.class))),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "사용자 권한만 접근할 수 있습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<UserProfileResDto> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUsername();
        UserProfileResDto userProfileResDto = userQueryService.getProfile(email);

        return new ResponseEntity<>(userProfileResDto, HttpStatus.OK);
    }
}
