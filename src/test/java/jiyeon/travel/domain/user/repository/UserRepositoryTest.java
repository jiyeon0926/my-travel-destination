package jiyeon.travel.domain.user.repository;

import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.global.common.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("별명으로 사용자를 조회합니다.")
    void findByDisplayName() {
        // given
        User user1 = createUser("user1@naver.com", "1234", "토끼", "010");
        User user2 = createUser("user2@naver.com", "1234", "고양이", "010");
        userRepository.saveAll(List.of(user1, user2));

        // when
        User user = userRepository.findByDisplayName("토끼").get();

        // then
        assertThat(user).extracting("email", "displayName")
                .contains(
                        "user1@naver.com", "토끼"
                );
    }

    @DisplayName("이메일로 사용자를 조회합니다.")
    @Test
    void findByEmail() {
        // given
        User user1 = createUser("user1@naver.com", "1234", "토끼", "010");
        User user2 = createUser("user2@naver.com", "1234", "고양이", "010");
        userRepository.saveAll(List.of(user1, user2));

        // when
        User user = userRepository.findByEmail("user2@naver.com").get();

        // then
        assertThat(user).extracting("email", "displayName")
                .contains(
                        "user2@naver.com", "고양이"
                );
    }

    private User createUser(String email, String password, String displayName, String phone) {
        return new User(email, password, displayName, phone, UserRole.USER);
    }
}