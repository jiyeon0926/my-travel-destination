package jiyeon.travel.domain.user.controller;

import jiyeon.travel.domain.auth.service.AuthService;
import jiyeon.travel.domain.user.dto.UserProfileResDto;
import jiyeon.travel.domain.user.dto.UserProfileUpdateReqDto;
import jiyeon.travel.domain.user.service.UserService;
import jiyeon.travel.global.auth.AuthenticationScheme;
import jiyeon.travel.global.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String email = userDetails.getUsername();
        userService.deleteUser(email);

        String accessToken = authorizationHeader.replace(AuthenticationScheme.generateType(AuthenticationScheme.BEARER), "");
        authService.logout(accessToken);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResDto> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUsername();
        UserProfileResDto userProfileResDto = userService.getProfile(email);

        return new ResponseEntity<>(userProfileResDto, HttpStatus.OK);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserProfileResDto> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @RequestBody UserProfileUpdateReqDto userProfileUpdateReqDto) {
        String email = userDetails.getUsername();
        UserProfileResDto userProfileResDto = userService.updateProfile(email, userProfileUpdateReqDto.getNickname(), userProfileUpdateReqDto.getPhone());

        return new ResponseEntity<>(userProfileResDto, HttpStatus.OK);
    }
}
