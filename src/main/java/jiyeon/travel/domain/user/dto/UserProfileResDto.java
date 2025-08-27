package jiyeon.travel.domain.user.dto;

import jiyeon.travel.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserProfileResDto {

    private final Long id;
    private final String email;
    private final String nickname;
    private final String phone;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public UserProfileResDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getDisplayName();
        this.phone = user.getPhone();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
