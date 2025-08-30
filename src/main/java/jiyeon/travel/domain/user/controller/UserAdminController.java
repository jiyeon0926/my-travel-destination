package jiyeon.travel.domain.user.controller;

import jiyeon.travel.domain.user.dto.UserListResDto;
import jiyeon.travel.domain.user.service.UserAdminService;
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

    private final UserAdminService userAdminService;

    @GetMapping("/search")
    public ResponseEntity<List<UserListResDto>> searchUsers(@RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(required = false) String email,
                                                            @RequestParam(required = false) String nickname,
                                                            @RequestParam(required = false) Boolean isDeleted) {
        List<UserListResDto> userListResDtos = userAdminService.searchUsers(page, size, email, nickname, isDeleted);

        return new ResponseEntity<>(userListResDtos, HttpStatus.OK);
    }
}
