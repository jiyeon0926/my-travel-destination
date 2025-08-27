package jiyeon.travel.domain.partner.service;

import jiyeon.travel.domain.partner.dto.PartnerProfileResDto;
import jiyeon.travel.domain.partner.dto.PartnerSignupResDto;
import jiyeon.travel.domain.partner.entity.Partner;
import jiyeon.travel.domain.partner.repository.PartnerRepository;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.domain.user.repository.UserRepository;
import jiyeon.travel.global.common.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PartnerAdminService {

    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public PartnerSignupResDto signupPartner(String email, String password, String name, String businessNumber, String address, String phone) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다.");
                });

        partnerRepository.findByBusinessNumber(businessNumber)
                .ifPresent(partner -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "동일한 사업장번호가 존재합니다.");
                });

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(email, encodedPassword, name, phone, UserRole.PARTNER);
        User savedUser = userRepository.save(user);

        Partner partner = new Partner(savedUser, businessNumber, address);
        Partner savedPartner = partnerRepository.save(partner);

        return new PartnerSignupResDto(savedUser, savedPartner);
    }

    @Transactional
    public PartnerProfileResDto updatePartnerById(Long partnerId, String name, String phone, String address) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "업체를 찾을 수 없습니다."));

        partner.updateProfile(name, phone, address);

        return new PartnerProfileResDto(partner.getUser(), partner);
    }
}
