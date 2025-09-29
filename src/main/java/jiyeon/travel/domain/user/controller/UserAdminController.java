package jiyeon.travel.domain.user.controller;

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
public class UserAdminController {

    private final UserAdminQueryService userAdminQueryService;

    @GetMapping("/search")
    public ResponseEntity<List<UserDetailResDto>> searchUsers(@RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @RequestParam(required = false) String email,
                                                              @RequestParam(required = false) String nickname,
                                                              @RequestParam(required = false) Boolean isDeleted) {
        List<UserDetailResDto> userDetailResDtoList = userAdminQueryService.searchUsers(page, size, email, nickname, isDeleted);

        return new ResponseEntity<>(userDetailResDtoList, HttpStatus.OK);
    }
}
