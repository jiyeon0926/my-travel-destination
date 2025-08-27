package jiyeon.travel.domain.partner.repository;

import jiyeon.travel.domain.partner.entity.Partner;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.domain.user.repository.UserRepository;
import jiyeon.travel.global.common.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class PartnerRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Test
    @DisplayName("사업장번호로 업체를 조회합니다.")
    void findByBusinessNumber() {
        // given
        User user1 = createUser("user1@naver.com", "1234", "토끼", "010");
        User savedUser1 = userRepository.save(user1);

        Partner partner1 = createPartner(savedUser1, "123-456", "서울특별시");
        partnerRepository.save(partner1);

        // when
        Partner partner = partnerRepository.findByBusinessNumber("123-456").get();

        // then
        assertThat(partner).extracting("businessNumber", "address")
                .contains(
                        "123-456", "서울특별시"
                );
    }

    private User createUser(String email, String password, String displayName, String phone) {
        return new User(email, password, displayName, phone, UserRole.PARTNER);
    }

    private Partner createPartner(User user, String businessNumber, String address) {
        return new Partner(user, businessNumber, address);
    }
}