package jiyeon.travel.domain.ticket.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jiyeon.travel.domain.ticket.entity.QTicket;
import jiyeon.travel.domain.ticket.entity.QTicketOption;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomTicketRepositoryImpl implements CustomTicketRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Ticket> findByIdAndEmailWithUserAndOption(Long id, String email) {
        QUser user = QUser.user;
        QTicket ticket = QTicket.ticket;
        QTicketOption ticketOption = QTicketOption.ticketOption;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(ticket.id.eq(id));
        conditions.and(user.email.eq(email));

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(ticket)
                .from(ticket)
                .innerJoin(user).on(ticket.user.id.eq(user.id)).fetchJoin()
                .leftJoin(ticketOption).on(ticket.id.eq(ticketOption.ticket.id)).fetchJoin()
                .where(conditions)
                .fetchOne());
    }

    @Override
    public Optional<Ticket> findByIdAndEmail(Long id, String email) {
        QUser user = QUser.user;
        QTicket ticket = QTicket.ticket;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(ticket.id.eq(id));
        conditions.and(user.email.eq(email));

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(ticket)
                .innerJoin(user).on(ticket.user.id.eq(user.id)).fetchJoin()
                .where(conditions)
                .fetchOne());
    }
}
