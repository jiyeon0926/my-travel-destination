package jiyeon.travel.domain.partner.repository;

import jiyeon.travel.domain.partner.dto.PartnerSimpleResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPartnerRepository {

    Page<PartnerSimpleResDto> searchPartners(Pageable pageable, String name);
}
