package jiyeon.travel.domain.user.service;

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

    @Transactional
    public UserProfileResDto updateProfile(String email, String nickname, String phone) {
        User user = userRepository.findActiveByEmailOrElseThrow(email);
        acceptIfNotNull(nickname, user::changeName);
        acceptIfNotNull(phone, user::changePhone);

        return new UserProfileResDto(user);
    }

    @Transactional
    public void deleteUser(String email) {
        User user = userRepository.findActiveByEmailOrElseThrow(email);
        user.changeIsDeleted();
    }

    private <T> void acceptIfNotNull(T t, Consumer<T> consumer) {
        if (t != null) consumer.accept(t);
    }
}
