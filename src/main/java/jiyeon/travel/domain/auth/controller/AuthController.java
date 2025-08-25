package jiyeon.travel.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jiyeon.travel.domain.auth.dto.LoginReqDto;
import jiyeon.travel.domain.auth.dto.LoginResDto;
import jiyeon.travel.domain.auth.dto.UserSignupReqDto;
import jiyeon.travel.domain.auth.dto.UserSignupResDto;
import jiyeon.travel.domain.auth.service.AuthService;
import jiyeon.travel.global.auth.AuthenticationScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
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
    public ResponseEntity<LoginResDto> login(@Valid @RequestBody LoginReqDto loginReqDto) {
        LoginResDto loginResDto = authService.login(
                loginReqDto.getEmail(),
                loginReqDto.getPassword()
        );

        return new ResponseEntity<>(loginResDto, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String accessToken = getAccessToken(request);
        Optional.ofNullable(accessToken).ifPresent(authService::logout);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String getAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        String headerPrefix = AuthenticationScheme.generateType(AuthenticationScheme.BEARER);

        return StringUtils.hasText(bearerToken) && bearerToken.startsWith(headerPrefix)
                ? bearerToken.substring(headerPrefix.length())
                : null;
    }
}
