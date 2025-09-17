package jiyeon.travel.domain.ticket.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jiyeon.travel.domain.ticket.entity.QTicket;
import jiyeon.travel.domain.ticket.entity.QTicketSchedule;
import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import jiyeon.travel.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomTicketScheduleRepositoryImpl implements CustomTicketScheduleRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<TicketSchedule> findByIdAndTicketIdAndEmail(Long id, Long ticketId, String email) {
        QUser user = QUser.user;
        QTicket ticket = QTicket.ticket;
        QTicketSchedule ticketSchedule = QTicketSchedule.ticketSchedule;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(ticketSchedule.id.eq(id));
        conditions.and(ticket.id.eq(ticketId));
        conditions.and(user.email.eq(email));

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(ticketSchedule)
                .innerJoin(ticketSchedule.ticket, ticket).fetchJoin()
                .innerJoin(ticket.user, user).fetchJoin()
                .where(conditions)
                .fetchOne());
    }
}
