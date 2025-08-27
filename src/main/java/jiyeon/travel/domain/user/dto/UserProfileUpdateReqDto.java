package jiyeon.travel.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserProfileUpdateReqDto {

    private final String nickname;
    private final String phone;
}
