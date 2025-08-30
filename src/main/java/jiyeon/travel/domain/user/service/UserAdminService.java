package jiyeon.travel.domain.user.service;

import jiyeon.travel.domain.user.dto.UserListResDto;
import jiyeon.travel.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserListResDto> searchUsers(int page, int size, String email, String nickname, Boolean isDeleted) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return userRepository.searchUsers(pageable, email, nickname, isDeleted).stream().toList();
    }
}
