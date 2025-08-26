package jiyeon.travel.domain.user.service;

import jiyeon.travel.domain.user.dto.PartnerSignupResDto;
import jiyeon.travel.domain.user.entity.Partner;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.domain.user.repository.PartnerRepository;
import jiyeon.travel.domain.user.repository.UserRepository;
import jiyeon.travel.global.common.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public PartnerSignupResDto signupPartner(String email, String password, String name, String businessNumber, String address, String phone) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(email, encodedPassword, name, phone, UserRole.PARTNER);
        User savedUser = userRepository.save(user);

        Partner partner = new Partner(savedUser, businessNumber, address);
        Partner savedPartner = partnerRepository.save(partner);

        return new PartnerSignupResDto(savedUser, savedPartner);
    }
}
