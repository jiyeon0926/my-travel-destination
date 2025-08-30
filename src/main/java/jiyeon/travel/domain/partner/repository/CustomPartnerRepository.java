package jiyeon.travel.domain.partner.repository;

import jiyeon.travel.domain.partner.dto.PartnerListResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPartnerRepository {

    Page<PartnerListResDto> searchPartners(Pageable pageable, String name);
}
