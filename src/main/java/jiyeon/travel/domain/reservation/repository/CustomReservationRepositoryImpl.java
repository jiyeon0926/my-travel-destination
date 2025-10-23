package jiyeon.travel.domain.reservation.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jiyeon.travel.domain.reservation.entity.QReservation;
import jiyeon.travel.domain.reservation.entity.QReservationOption;
import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.domain.ticket.entity.QTicket;
import jiyeon.travel.domain.ticket.entity.QTicketSchedule;
import jiyeon.travel.domain.user.entity.QUser;
import jiyeon.travel.global.common.enums.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

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
                .innerJoin(reservation.ticketSchedule, ticketSchedule).fetchJoin()
                .innerJoin(ticketSchedule.ticket, ticket).fetchJoin()
                .where(reservation.id.eq(id))
                .fetchOne());
    }

    @Override
    public Optional<Reservation> findByIdAndPartnerEmailWithTicketAndSchedule(Long id, String email) {
        QReservation reservation = QReservation.reservation;
        QTicket ticket = QTicket.ticket;
        QTicketSchedule ticketSchedule = QTicketSchedule.ticketSchedule;
        QUser user = QUser.user;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(reservation.id.eq(id));
        conditions.and(user.email.eq(email));

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(reservation)
                .innerJoin(reservation.ticketSchedule, ticketSchedule).fetchJoin()
                .innerJoin(ticketSchedule.ticket, ticket).fetchJoin()
                .innerJoin(ticket.user, user).fetchJoin()
                .where(conditions)
                .fetchOne());
    }

    @Override
    public Optional<Reservation> findByIdAndUserEmailWithSchedule(Long id, String email) {
        QReservation reservation = QReservation.reservation;
        QReservationOption reservationOption = QReservationOption.reservationOption;
        QTicketSchedule ticketSchedule = QTicketSchedule.ticketSchedule;
        QUser user = QUser.user;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(reservation.id.eq(id));
        conditions.and(user.email.eq(email));

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(reservation)
                .leftJoin(reservation.reservationOptions, reservationOption).fetchJoin()
                .innerJoin(reservation.user, user).fetchJoin()
                .innerJoin(reservation.ticketSchedule, ticketSchedule).fetchJoin()
                .where(conditions)
                .fetchOne());
    }

    @Override
    public Optional<Reservation> findByIdAndPartnerEmailWithTicketAndScheduleWithoutUnpaid(Long id, String email) {
        QReservation reservation = QReservation.reservation;
        QReservationOption reservationOption = QReservationOption.reservationOption;
        QTicket ticket = QTicket.ticket;
        QTicketSchedule ticketSchedule = QTicketSchedule.ticketSchedule;
        QUser user = QUser.user;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(reservation.id.eq(id));
        conditions.and(user.email.eq(email));
        conditions.and(reservation.status.ne(ReservationStatus.UNPAID));

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(reservation)
                .leftJoin(reservation.reservationOptions, reservationOption).fetchJoin()
                .innerJoin(reservation.ticketSchedule, ticketSchedule).fetchJoin()
                .innerJoin(ticketSchedule.ticket, ticket).fetchJoin()
                .innerJoin(ticket.user, user).fetchJoin()
                .where(conditions)
                .fetchOne());
    }

    @Override
    public List<Reservation> findAllByTicketIdWithTicketAndSchedule(Long ticketId) {
        QReservation reservation = QReservation.reservation;
        QTicket ticket = QTicket.ticket;
        QTicketSchedule ticketSchedule = QTicketSchedule.ticketSchedule;

        return jpaQueryFactory
                .selectFrom(reservation)
                .innerJoin(reservation.ticketSchedule, ticketSchedule).fetchJoin()
                .innerJoin(ticketSchedule.ticket, ticket).fetchJoin()
                .where(ticket.id.eq(ticketId))
                .fetch();
    }

    @Override
    public List<Reservation> findAllByPartnerEmailWithoutUnpaid(String email, Pageable pageable) {
        QReservation reservation = QReservation.reservation;
        QTicket ticket = QTicket.ticket;
        QTicketSchedule ticketSchedule = QTicketSchedule.ticketSchedule;
        QUser user = QUser.user;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(user.email.eq(email));
        conditions.and(reservation.status.ne(ReservationStatus.UNPAID));
        conditions.and(reservation.status.ne(ReservationStatus.EXPIRED));

        return jpaQueryFactory
                .selectFrom(reservation)
                .innerJoin(reservation.ticketSchedule, ticketSchedule).fetchJoin()
                .innerJoin(ticketSchedule.ticket, ticket).fetchJoin()
                .innerJoin(ticket.user, user).fetchJoin()
                .where(conditions)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
