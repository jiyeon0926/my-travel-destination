package jiyeon.travel.domain.user.service;

import jiyeon.travel.domain.auth.service.AuthService;
import jiyeon.travel.domain.user.dto.UserProfileResDto;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;
    private final AuthService authService;

    @Transactional
    public UserProfileResDto updateProfile(String email, String nickname, String phone) {
        User user = userRepository.findActiveByEmailOrElseThrow(email);
        acceptIfNotNull(nickname, user::changeName);
        acceptIfNotNull(phone, user::changePhone);

        return new UserProfileResDto(user);
    }

    @Transactional
    public void deleteUser(String email, String accessToken) {
        User user = userRepository.findActiveByEmailOrElseThrow(email);
        user.changeIsDeleted();
        authService.logout(accessToken);
    }

    private <T> void acceptIfNotNull(T t, Consumer<T> consumer) {
        if (t != null) consumer.accept(t);
    }
}
