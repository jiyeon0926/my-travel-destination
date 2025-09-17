package jiyeon.travel.domain.ticket.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jiyeon.travel.domain.ticket.entity.QTicket;
import jiyeon.travel.domain.ticket.entity.QTicketOption;
import jiyeon.travel.domain.ticket.entity.TicketOption;
import jiyeon.travel.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomTicketOptionRepositoryImpl implements CustomTicketOptionRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<TicketOption> findByIdAndTicketIdAndEmail(Long id, Long ticketId, String email) {
        QUser user = QUser.user;
        QTicket ticket = QTicket.ticket;
        QTicketOption ticketOption = QTicketOption.ticketOption;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(ticketOption.id.eq(id));
        conditions.and(ticket.id.eq(ticketId));
        conditions.and(user.email.eq(email));

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(ticketOption)
                .innerJoin(ticketOption.ticket, ticket).fetchJoin()
                .innerJoin(ticket.user, user).fetchJoin()
                .where(conditions)
                .fetchOne());
    }
}
