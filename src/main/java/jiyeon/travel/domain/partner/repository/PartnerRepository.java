package jiyeon.travel.domain.partner.repository;

import jiyeon.travel.domain.partner.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    Optional<Partner> findByBusinessNumber(String businessNumber);

    @Query(value = "select p from Partner p inner join fetch p.user u where u.email = :email and u.isDeleted = false")
    Optional<Partner> findByEmailAndIsDeletedFalse(String email);
}
