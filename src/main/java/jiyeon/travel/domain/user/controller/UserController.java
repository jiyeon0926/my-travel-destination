package jiyeon.travel.domain.user.controller;

import jiyeon.travel.domain.auth.service.AuthService;
import jiyeon.travel.domain.user.service.UserService;
import jiyeon.travel.global.auth.AuthenticationScheme;
import jiyeon.travel.global.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
