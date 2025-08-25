package jiyeon.travel.domain.auth.controller;

import jakarta.validation.Valid;
import jiyeon.travel.domain.auth.dto.UserSignupReqDto;
import jiyeon.travel.domain.auth.dto.UserSignupResDto;
import jiyeon.travel.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
