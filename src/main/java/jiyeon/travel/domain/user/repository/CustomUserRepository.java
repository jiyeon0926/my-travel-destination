package jiyeon.travel.domain.user.repository;

import jiyeon.travel.domain.user.dto.UserDetailResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomUserRepository {

    Page<UserDetailResDto> searchUsers(Pageable pageable, String email, String nickname, Boolean isDeleted);
}
