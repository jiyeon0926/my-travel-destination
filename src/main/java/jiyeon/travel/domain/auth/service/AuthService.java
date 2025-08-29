package jiyeon.travel.domain.auth.service;

import jiyeon.travel.domain.auth.dto.LoginResDto;
import jiyeon.travel.domain.auth.dto.UserSignupResDto;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.domain.user.repository.UserRepository;
import jiyeon.travel.global.auth.AuthenticationScheme;
import jiyeon.travel.global.auth.jwt.JwtProvider;
import jiyeon.travel.global.common.enums.UserRole;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final BlacklistService blacklistService;

    @Transactional
    public UserSignupResDto signupUser(String email, String password, String nickname, String phone) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
                });

        userRepository.findByDisplayName(nickname)
                .ifPresent(user -> {
                    throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
                });

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(email, encodedPassword, nickname, phone, UserRole.USER);
        User savedUser = userRepository.save(user);

        return new UserSignupResDto(savedUser);
    }

    public LoginResDto login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        validatePassword(password, user.getPassword());

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtProvider.generateAccessToken(authentication);

        return new LoginResDto(AuthenticationScheme.BEARER.getName(), accessToken);
    }

    public void logout(String accessToken) {
        blacklistService.saveAccessToken(accessToken);
    }

    @Transactional
    public void updatePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        validatePassword(oldPassword, user.getPassword());

        if (oldPassword.equals(newPassword)) {
            throw new CustomException(ErrorCode.PASSWORD_SAME_AS_OLD);
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.changePassword(encodedPassword);
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        boolean isNotValid = !passwordEncoder.matches(rawPassword, encodedPassword);

        if (isNotValid) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }
    }
}
