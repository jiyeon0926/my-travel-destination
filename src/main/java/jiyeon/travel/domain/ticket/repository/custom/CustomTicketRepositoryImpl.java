package jiyeon.travel.domain.ticket.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jiyeon.travel.domain.ticket.dto.QTicketSimpleResDto;
import jiyeon.travel.domain.ticket.dto.TicketListResDto;
import jiyeon.travel.domain.ticket.dto.TicketSimpleResDto;
import jiyeon.travel.domain.ticket.entity.QTicket;
import jiyeon.travel.domain.ticket.entity.QTicketOption;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomTicketRepositoryImpl implements CustomTicketRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Ticket> findByIdAndEmailWithOption(Long id, String email) {
        QUser user = QUser.user;
        QTicket ticket = QTicket.ticket;
        QTicketOption ticketOption = QTicketOption.ticketOption;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(ticket.id.eq(id));
        conditions.and(user.email.eq(email));

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(ticket)
                .innerJoin(ticket.user, user).fetchJoin()
                .leftJoin(ticket.ticketOptions, ticketOption).fetchJoin()
                .where(conditions)
                .fetchOne());
    }

    @Override
    public TicketListResDto findAllByEmail(Pageable pageable, String email) {
        QUser user = QUser.user;
        QTicket ticket = QTicket.ticket;

        Long total = Optional.ofNullable(
                        jpaQueryFactory
                                .select(ticket.count())
                                .from(ticket)
                                .innerJoin(ticket.user, user).fetchJoin()
                                .where(user.email.eq(email))
                                .fetchOne())
                .orElse(0L);

        List<TicketSimpleResDto> tickets = jpaQueryFactory
                .select(new QTicketSimpleResDto(
                        ticket.id,
                        ticket.name,
                        ticket.saleStartDate,
                        ticket.saleEndDate,
                        ticket.saleStatus.stringValue(),
                        ticket.createdAt,
                        ticket.updatedAt
                ))
                .from(ticket)
                .innerJoin(ticket.user, user).fetchJoin()
                .where(user.email.eq(email))
                .orderBy(ticket.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new TicketListResDto(total, tickets);
    }
}
