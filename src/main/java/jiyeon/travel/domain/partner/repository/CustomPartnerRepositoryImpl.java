package jiyeon.travel.domain.partner.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jiyeon.travel.domain.partner.dto.PartnerSimpleResDto;
import jiyeon.travel.domain.partner.dto.QPartnerSimpleResDto;
import jiyeon.travel.domain.partner.entity.QPartner;
import jiyeon.travel.domain.user.entity.QUser;
import jiyeon.travel.global.common.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomPartnerRepositoryImpl implements CustomPartnerRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PartnerSimpleResDto> searchPartners(Pageable pageable, String name) {
        QUser user = QUser.user;
        QPartner partner = QPartner.partner;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(user.role.eq(UserRole.PARTNER));
        conditions.and(user.isDeleted.eq(false));

        if (name != null) {
            conditions.and(user.displayName.contains(name));
        }

        List<PartnerSimpleResDto> partners = jpaQueryFactory
                .select(new QPartnerSimpleResDto(
                        partner.id,
                        user.displayName,
                        partner.businessNumber,
                        user.createdAt
                ))
                .from(partner)
                .innerJoin(user).on(partner.user.id.eq(user.id))
                .where(conditions)
                .orderBy(user.displayName.asc(), partner.businessNumber.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(jpaQueryFactory
                .select(partner.count())
                .from(partner)
                .innerJoin(user).on(partner.user.id.eq(user.id)).fetchJoin()
                .where(conditions)
                .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(partners, pageable, total);
    }
}
