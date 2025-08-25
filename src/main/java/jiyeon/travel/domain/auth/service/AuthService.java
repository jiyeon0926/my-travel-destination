package jiyeon.travel.domain.auth.service;

import jiyeon.travel.domain.auth.dto.UserSignupResDto;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserSignupResDto signupUser(String email, String password, String nickname, String phone) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다.");
                });

        userRepository.findByDisplayName(nickname)
                .ifPresent(user -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 별명입니다.");
                });

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(email, encodedPassword, nickname, phone);
        User savedUser = userRepository.save(user);

        return new UserSignupResDto(savedUser);
    }
}
