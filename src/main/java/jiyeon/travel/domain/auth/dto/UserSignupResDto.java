package jiyeon.travel.domain.auth.dto;

import jiyeon.travel.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserSignupResDto {

    private final Long id;
    private final String email;
    private final String nickname;
    private final String phone;
    private final String role;

    public UserSignupResDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getDisplayName();
        this.phone = user.getPhone();
        this.role = user.getRole().name();
    }
}
