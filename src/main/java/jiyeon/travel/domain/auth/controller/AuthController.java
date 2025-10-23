package jiyeon.travel.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jiyeon.travel.domain.auth.dto.*;
import jiyeon.travel.domain.auth.service.AuthService;
import jiyeon.travel.global.auth.AuthenticationScheme;
import jiyeon.travel.global.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(
        name = "Auth",
        description = "회원가입, 로그인, 로그아웃, 비밀번호 변경 기능을 제공합니다."
)
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "이메일, 비밀번호, 별명, 전화번호를 입력해야 합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserSignupResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일 또는 별명 입니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<UserSignupResDto> signupUser(@Valid @RequestBody UserSignupReqDto userSignupReqDto) {
        UserSignupResDto userSignupResDto = authService.signupUser(
                userSignupReqDto.getEmail(),
                userSignupReqDto.getPassword(),
                userSignupReqDto.getNickname(),
                userSignupReqDto.getPhone()
        );

        return new ResponseEntity<>(userSignupResDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호를 입력해 로그인을 요청하면 토큰을 발급받을 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<LoginResDto> login(@Valid @RequestBody LoginReqDto loginReqDto) {
        LoginResDto loginResDto = authService.login(
                loginReqDto.getEmail(),
                loginReqDto.getPassword()
        );

        return new ResponseEntity<>(loginResDto, HttpStatus.OK);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "토큰을 무효화하여 해당 토큰으로의 인증 요청을 거부하도록 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String accessToken = authorizationHeader.replace(AuthenticationScheme.generateType(AuthenticationScheme.BEARER), "");
        authService.logout(accessToken);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/password")
    @Operation(summary = "비밀번호 변경", description = "비밀번호는 최소 8자 이상, 대문자, 소문자, 숫자, 특수문자를 포함하여 변경해야 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "JWT 인증이 필요합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @Valid @RequestBody PasswordUpdateReqDto passwordUpdateReqDto) {
        String email = userDetails.getUsername();
        authService.updatePassword(email, passwordUpdateReqDto.getOldPassword(), passwordUpdateReqDto.getNewPassword());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
