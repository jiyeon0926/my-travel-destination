package jiyeon.travel.domain.reservation.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jiyeon.travel.domain.reservation.entity.QReservation;
import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.domain.ticket.entity.QTicket;
import jiyeon.travel.domain.ticket.entity.QTicketSchedule;
import jiyeon.travel.domain.user.entity.QUser;
import jiyeon.travel.global.common.enums.ReservationStatus;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomReservationRepositoryImpl implements CustomReservationRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Reservation> findByIdWithTicketAndSchedule(Long id) {
        QReservation reservation = QReservation.reservation;
        QTicket ticket = QTicket.ticket;
        QTicketSchedule ticketSchedule = QTicketSchedule.ticketSchedule;

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(reservation)
                .innerJoin(ticketSchedule).on(reservation.ticketSchedule.id.eq(ticketSchedule.id)).fetchJoin()
                .innerJoin(ticket).on(ticketSchedule.ticket.id.eq(ticket.id)).fetchJoin()
                .where(reservation.id.eq(id))
                .fetchOne());
    }

    @Override
    public List<Reservation> findAllByTicketIdWithTicketAndSchedule(Long ticketId) {
        QReservation reservation = QReservation.reservation;
        QTicket ticket = QTicket.ticket;
        QTicketSchedule ticketSchedule = QTicketSchedule.ticketSchedule;

        return jpaQueryFactory
                .selectFrom(reservation)
                .innerJoin(ticketSchedule).on(reservation.ticketSchedule.id.eq(ticketSchedule.id)).fetchJoin()
                .innerJoin(ticket).on(ticketSchedule.ticket.id.eq(ticket.id)).fetchJoin()
                .where(ticket.id.eq(ticketId))
                .fetch();
    }

    @Override
    public Optional<Reservation> findByIdAndEmailWithTicketAndSchedule(Long id, String email) {
        QReservation reservation = QReservation.reservation;
        QTicket ticket = QTicket.ticket;
        QTicketSchedule ticketSchedule = QTicketSchedule.ticketSchedule;
        QUser user = QUser.user;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(reservation.id.eq(id));
        conditions.and(user.email.eq(email));

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(reservation)
                .innerJoin(ticketSchedule).on(reservation.ticketSchedule.id.eq(ticketSchedule.id)).fetchJoin()
                .innerJoin(ticket).on(ticketSchedule.ticket.id.eq(ticket.id)).fetchJoin()
                .innerJoin(user).on(ticket.user.id.eq(user.id)).fetchJoin()
                .where(conditions)
                .fetchOne());
    }

    @Override
    public List<Reservation> findAllWithoutUnpaid(String email) {
        QReservation reservation = QReservation.reservation;
        QTicket ticket = QTicket.ticket;
        QTicketSchedule ticketSchedule = QTicketSchedule.ticketSchedule;
        QUser user = QUser.user;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(user.email.eq(email));
        conditions.and(reservation.status.ne(ReservationStatus.UNPAID));

        return jpaQueryFactory
                .selectFrom(reservation)
                .innerJoin(ticketSchedule).on(reservation.ticketSchedule.id.eq(ticketSchedule.id)).fetchJoin()
                .innerJoin(ticket).on(ticketSchedule.ticket.id.eq(ticket.id)).fetchJoin()
                .innerJoin(user).on(ticket.user.id.eq(user.id)).fetchJoin()
                .where(conditions)
                .fetch();
    }
}
