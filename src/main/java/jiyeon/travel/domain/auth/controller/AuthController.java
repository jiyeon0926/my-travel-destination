package jiyeon.travel.domain.auth.controller;

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
    public ResponseEntity<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String accessToken = authorizationHeader.replace(AuthenticationScheme.generateType(AuthenticationScheme.BEARER), "");
        authService.logout(accessToken);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @Valid @RequestBody PasswordUpdateReqDto passwordUpdateReqDto) {
        String email = userDetails.getUsername();
        authService.updatePassword(email, passwordUpdateReqDto.getOldPassword(), passwordUpdateReqDto.getNewPassword());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
