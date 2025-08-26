package jiyeon.travel.domain.user.repository;

import jiyeon.travel.domain.user.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    Optional<Partner> findByBusinessNumber(String businessNumber);
}
