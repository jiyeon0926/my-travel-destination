package jiyeon.travel.domain.partner.repository;

import jiyeon.travel.domain.partner.entity.Partner;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    Optional<Partner> findByBusinessNumber(String businessNumber);

    @Query(value = "select p from Partner p inner join fetch p.user u where u.email = :email and u.isDeleted = false")
    Optional<Partner> findByEmailAndIsDeletedFalse(String email);

    default Partner findByIdOrElseThrow(Long id) {
        return this.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PARTNER_NOT_FOUND));
    }
}
