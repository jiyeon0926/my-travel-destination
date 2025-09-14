package jiyeon.travel.domain.ticket.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jiyeon.travel.domain.reservation.entity.QReservation;
import jiyeon.travel.domain.ticket.dto.QTicketSimpleResDto;
import jiyeon.travel.domain.ticket.dto.TicketListResDto;
import jiyeon.travel.domain.ticket.dto.TicketSimpleResDto;
import jiyeon.travel.domain.ticket.entity.QTicket;
import jiyeon.travel.domain.ticket.entity.QTicketOption;
import jiyeon.travel.domain.ticket.entity.QTicketSchedule;
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
    public TicketListResDto findAllByEmail(Pageable pageable, String email) {
        QUser user = QUser.user;
        QTicket ticket = QTicket.ticket;

        Long total = Optional.ofNullable(
                        jpaQueryFactory
                                .select(ticket.count())
                                .from(ticket)
                                .innerJoin(user).on(ticket.user.id.eq(user.id)).fetchJoin()
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
                .innerJoin(user).on(ticket.user.id.eq(user.id)).fetchJoin()
                .where(user.email.eq(email))
                .orderBy(ticket.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new TicketListResDto(total, tickets);
    }

    @Override
    public Ticket getTicketByReservationId(Long reservationId) {
        QTicket ticket = QTicket.ticket;
        QTicketSchedule ticketSchedule = QTicketSchedule.ticketSchedule;
        QReservation reservation = QReservation.reservation;

        return jpaQueryFactory
                .selectFrom(ticket)
                .innerJoin(ticketSchedule).on(ticket.id.eq(ticketSchedule.ticket.id)).fetchJoin()
                .innerJoin(reservation).on(ticketSchedule.id.eq(reservation.ticketSchedule.id)).fetchJoin()
                .where(reservation.id.eq(reservationId))
                .fetchOne();
    }
}
