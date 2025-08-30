package jiyeon.travel.domain.user.repository;

import jiyeon.travel.domain.user.dto.UserListResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomUserRepository {

    Page<UserListResDto> searchUsers(Pageable pageable, String email, String nickname, Boolean isDeleted);
}
