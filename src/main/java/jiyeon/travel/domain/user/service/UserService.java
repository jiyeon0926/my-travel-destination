package jiyeon.travel.domain.user.service;

import jiyeon.travel.domain.user.dto.UserProfileResDto;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void deleteUser(String email) {
        userRepository.findByEmail(email)
                .filter(u -> !u.isDeleted())
                .ifPresent(User::changeIsDeleted);
    }

    @Transactional(readOnly = true)
    public UserProfileResDto getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        return new UserProfileResDto(user);
    }
}
