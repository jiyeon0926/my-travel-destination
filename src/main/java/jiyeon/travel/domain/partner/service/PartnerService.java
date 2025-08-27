package jiyeon.travel.domain.partner.service;

import jiyeon.travel.domain.partner.dto.PartnerProfileResDto;
import jiyeon.travel.domain.partner.entity.Partner;
import jiyeon.travel.domain.partner.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerRepository;

    @Transactional(readOnly = true)
    public PartnerProfileResDto getProfile(String email) {
        Partner partner = partnerRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "업체를 찾을 수 없습니다."));

        return new PartnerProfileResDto(partner.getUser(), partner);
    }
}
