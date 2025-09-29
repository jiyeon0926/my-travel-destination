package jiyeon.travel.domain.user.controller;

import jiyeon.travel.domain.user.dto.UserProfileResDto;
import jiyeon.travel.domain.user.dto.UserProfileUpdateReqDto;
import jiyeon.travel.domain.user.service.UserCommandService;
import jiyeon.travel.domain.user.service.UserQueryService;
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

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    @PatchMapping("/me")
    public ResponseEntity<UserProfileResDto> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @RequestBody UserProfileUpdateReqDto userProfileUpdateReqDto) {
        String email = userDetails.getUsername();
        UserProfileResDto userProfileResDto = userCommandService.updateProfile(email, userProfileUpdateReqDto.getNickname(), userProfileUpdateReqDto.getPhone());

        return new ResponseEntity<>(userProfileResDto, HttpStatus.OK);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String email = userDetails.getUsername();
        String accessToken = authorizationHeader.replace(AuthenticationScheme.generateType(AuthenticationScheme.BEARER), "");

        userCommandService.deleteUser(email, accessToken);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResDto> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUsername();
        UserProfileResDto userProfileResDto = userQueryService.getProfile(email);

        return new ResponseEntity<>(userProfileResDto, HttpStatus.OK);
    }
}
