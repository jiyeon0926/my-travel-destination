package jiyeon.travel.domain.blog.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jiyeon.travel.domain.blog.dto.BlogTicketItemDto;
import jiyeon.travel.domain.blog.dto.QBlogTicketItemDto;
import jiyeon.travel.domain.blog.entity.QBlog;
import jiyeon.travel.domain.blog.entity.QBlogTicketItem;
import jiyeon.travel.domain.reservation.entity.QReservation;
import jiyeon.travel.domain.ticket.entity.QTicket;
import jiyeon.travel.domain.ticket.entity.QTicketSchedule;
import jiyeon.travel.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomBlogTicketItemRepositoryImpl implements CustomBlogTicketItemRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<BlogTicketItemDto> findDetailsByBlogIdAndEmail(Long blogId, String email) {
        QBlog blog = QBlog.blog;
        QUser user = QUser.user;
        QBlogTicketItem blogTicketItem = QBlogTicketItem.blogTicketItem;
        QReservation reservation = QReservation.reservation;
        QTicketSchedule ticketSchedule = QTicketSchedule.ticketSchedule;
        QTicket ticket = QTicket.ticket;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(blog.id.eq(blogId));
        conditions.and(user.email.eq(email));

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
                .innerJoin(blogTicketItem.blog, blog)
                .innerJoin(blog.user, user)
                .innerJoin(blogTicketItem.reservation, reservation)
                .innerJoin(reservation.ticketSchedule, ticketSchedule)
                .innerJoin(ticketSchedule.ticket, ticket)
                .where(conditions)
                .fetch();
    }
}
