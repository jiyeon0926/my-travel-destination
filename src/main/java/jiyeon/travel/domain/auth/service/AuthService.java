package jiyeon.travel.domain.auth.service;

import jiyeon.travel.domain.auth.dto.LoginResDto;
import jiyeon.travel.domain.auth.dto.UserSignupResDto;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.domain.user.repository.UserRepository;
import jiyeon.travel.global.auth.AuthenticationScheme;
import jiyeon.travel.global.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    public LoginResDto login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

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

    private void validatePassword(String rawPassword, String encodedPassword) {
        boolean isNotValid = !passwordEncoder.matches(rawPassword, encodedPassword);

        if (isNotValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다.");
        }
    }
}
