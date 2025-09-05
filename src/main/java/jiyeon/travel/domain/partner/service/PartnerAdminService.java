package jiyeon.travel.domain.partner.service;

import jiyeon.travel.domain.partner.dto.PartnerProfileResDto;
import jiyeon.travel.domain.partner.dto.PartnerSignupResDto;
import jiyeon.travel.domain.partner.dto.PartnerSimpleResDto;
import jiyeon.travel.domain.partner.entity.Partner;
import jiyeon.travel.domain.partner.repository.PartnerRepository;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.domain.user.repository.UserRepository;
import jiyeon.travel.global.common.enums.UserRole;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                    throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
                });

        partnerRepository.findByBusinessNumber(businessNumber)
                .ifPresent(partner -> {
                    throw new CustomException(ErrorCode.BUSINESS_NUMBER_ALREADY_EXISTS);
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
        Partner partner = partnerRepository.findByIdOrElseThrow(partnerId);

        if (name != null) partner.changeName(name);
        if (name != null) partner.changePhone(phone);
        if (name != null) partner.changeAddress(address);

        return new PartnerProfileResDto(partner.getUser(), partner);
    }

    @Transactional(readOnly = true)
    public PartnerProfileResDto findPartnerById(Long partnerId) {
        Partner partner = partnerRepository.findByIdOrElseThrow(partnerId);

        return new PartnerProfileResDto(partner.getUser(), partner);
    }

    @Transactional
    public void deletePartnerById(Long partnerId) {
        Partner partner = partnerRepository.findByIdOrElseThrow(partnerId);
        partnerRepository.delete(partner);
        userRepository.delete(partner.getUser());
    }

    @Transactional(readOnly = true)
    public List<PartnerSimpleResDto> searchPartners(int page, int size, String name) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return partnerRepository.searchPartners(pageable, name).stream().toList();
    }
}
