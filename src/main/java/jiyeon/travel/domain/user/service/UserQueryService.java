package jiyeon.travel.domain.user.service;

import jiyeon.travel.domain.user.dto.UserProfileResDto;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserProfileResDto getProfile(String email) {
        User user = userRepository.findActiveByEmailOrElseThrow(email);

        return new UserProfileResDto(user);
    }

    public User getActiveUserByEmail(String email) {
        return userRepository.findActiveByEmailOrElseThrow(email);
    }
}
