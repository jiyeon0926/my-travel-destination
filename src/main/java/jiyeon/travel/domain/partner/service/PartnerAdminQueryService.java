package jiyeon.travel.domain.partner.service;

import jiyeon.travel.domain.partner.dto.PartnerProfileResDto;
import jiyeon.travel.domain.partner.dto.PartnerSimpleResDto;
import jiyeon.travel.domain.partner.entity.Partner;
import jiyeon.travel.domain.partner.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartnerAdminQueryService {

    private final PartnerRepository partnerRepository;

    @Transactional(readOnly = true)
    public PartnerProfileResDto findPartnerById(Long partnerId) {
        Partner partner = partnerRepository.findByIdOrElseThrow(partnerId);

        return new PartnerProfileResDto(partner.getUser(), partner);
    }

    @Transactional(readOnly = true)
    public List<PartnerSimpleResDto> searchPartners(int page, int size, String name) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return partnerRepository.searchPartners(pageable, name).stream().toList();
    }
}
