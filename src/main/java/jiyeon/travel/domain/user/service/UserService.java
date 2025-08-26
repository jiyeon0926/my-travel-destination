package jiyeon.travel.domain.user.service;

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
        userRepository.findByEmail(email)
                .filter(u -> !u.isDeleted())
                .ifPresent(User::changeIsDeleted);
    }
}
