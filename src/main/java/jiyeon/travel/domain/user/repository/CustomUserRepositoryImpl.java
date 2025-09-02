package jiyeon.travel.domain.user.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jiyeon.travel.domain.user.dto.QUserDetailResDto;
import jiyeon.travel.domain.user.dto.UserDetailResDto;
import jiyeon.travel.domain.user.entity.QUser;
import jiyeon.travel.global.common.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<UserDetailResDto> searchUsers(Pageable pageable, String email, String nickname, Boolean isDeleted) {
        QUser user = QUser.user;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(user.role.eq(UserRole.USER));

        if (email != null) {
            conditions.and(user.email.contains(email));
        }

        if (nickname != null) {
            conditions.and(user.displayName.contains(nickname));
        }

        if (isDeleted != null) {
            conditions.and(user.isDeleted.eq(isDeleted));
        }

        List<UserDetailResDto> users = jpaQueryFactory
                .select(new QUserDetailResDto(
                        user.id,
                        user.email,
                        user.displayName,
                        user.phone,
                        user.isDeleted,
                        user.createdAt,
                        user.updatedAt))
                .from(user)
                .where(conditions)
                .orderBy(user.displayName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(jpaQueryFactory
                .select(user.count())
                .from(user)
                .where(conditions)
                .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(users, pageable, total);
    }
}
