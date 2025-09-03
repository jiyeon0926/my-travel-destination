package jiyeon.travel.domain.ticket.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jiyeon.travel.domain.ticket.entity.QTicket;
import jiyeon.travel.domain.ticket.entity.QTicketImage;
import jiyeon.travel.domain.ticket.entity.TicketImage;
import jiyeon.travel.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomTicketImageRepositoryImpl implements CustomTicketImageRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<TicketImage> findByIdAndTicketIdAndEmail(Long id, Long ticketId, String email) {
        QUser user = QUser.user;
        QTicket ticket = QTicket.ticket;
        QTicketImage ticketImage = QTicketImage.ticketImage;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(ticketImage.id.eq(id));
        conditions.and(ticket.id.eq(ticketId));
        conditions.and(user.email.eq(email));

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(ticketImage)
                .innerJoin(ticket).on(ticketImage.ticket.id.eq(ticket.id)).fetchJoin()
                .innerJoin(user).on(ticket.user.id.eq(user.id)).fetchJoin()
                .where(conditions)
                .fetchOne());
    }
}
