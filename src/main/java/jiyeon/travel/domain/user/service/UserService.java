package jiyeon.travel.domain.user.service;

import jiyeon.travel.domain.user.dto.UserProfileResDto;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void deleteUser(String email) {
        User user = userRepository.findActiveByEmailOrElseThrow(email);
        user.changeIsDeleted();
    }

    @Transactional(readOnly = true)
    public UserProfileResDto getProfile(String email) {
        User user = userRepository.findActiveByEmailOrElseThrow(email);

        return new UserProfileResDto(user);
    }

    @Transactional
    public UserProfileResDto updateProfile(String email, String nickname, String phone) {
        User user = userRepository.findActiveByEmailOrElseThrow(email);

        if (nickname != null) user.changeName(nickname);
        if (phone != null) user.changePhone(phone);

        return new UserProfileResDto(user);
    }

    public User getActiveUserByEmail(String email) {
        return userRepository.findActiveByEmailOrElseThrow(email);
    }
}
