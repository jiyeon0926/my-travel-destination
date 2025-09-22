package jiyeon.travel.domain.blog.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jiyeon.travel.domain.blog.dto.BlogTicketItemDto;
import jiyeon.travel.domain.blog.dto.QBlogTicketItemDto;
import jiyeon.travel.domain.blog.entity.QBlogTicketItem;
import jiyeon.travel.domain.reservation.entity.QReservation;
import jiyeon.travel.domain.ticket.entity.QTicket;
import jiyeon.travel.domain.ticket.entity.QTicketSchedule;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomBlogTicketItemRepositoryImpl implements CustomBlogTicketItemRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<BlogTicketItemDto> findDetailsByBlogId(Long blogId) {
        QBlogTicketItem blogTicketItem = QBlogTicketItem.blogTicketItem;
        QReservation reservation = QReservation.reservation;
        QTicketSchedule ticketSchedule = QTicketSchedule.ticketSchedule;
        QTicket ticket = QTicket.ticket;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(blogTicketItem.blog.id.eq(blogId));

        return jpaQueryFactory
                .select(new QBlogTicketItemDto(
                        blogTicketItem.id,
                        reservation.id,
                        ticket.name,
                        ticketSchedule.startDate,
                        ticketSchedule.startTime,
                        ticket.address,
                        reservation.totalQuantity,
                        reservation.totalAmount
                ))
                .from(blogTicketItem)
                .innerJoin(blogTicketItem.reservation, reservation)
                .innerJoin(reservation.ticketSchedule, ticketSchedule)
                .innerJoin(ticketSchedule.ticket, ticket)
                .where(conditions)
                .fetch();
    }
}
