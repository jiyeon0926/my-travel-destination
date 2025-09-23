package jiyeon.travel.domain.blog.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jiyeon.travel.domain.blog.dto.BlogTicketItemDto;
import jiyeon.travel.domain.blog.dto.QBlogTicketItemDto;
import jiyeon.travel.domain.blog.entity.BlogTicketItem;
import jiyeon.travel.domain.blog.entity.QBlog;
import jiyeon.travel.domain.blog.entity.QBlogTicketItem;
import jiyeon.travel.domain.reservation.entity.QReservation;
import jiyeon.travel.domain.ticket.entity.QTicket;
import jiyeon.travel.domain.ticket.entity.QTicketSchedule;
import jiyeon.travel.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomBlogTicketItemRepositoryImpl implements CustomBlogTicketItemRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<BlogTicketItem> findByIdAndBlogIdAndEmail(Long id, Long blogId, String email) {
        QBlogTicketItem blogTicketItem = QBlogTicketItem.blogTicketItem;
        QBlog blog = QBlog.blog;
        QUser user = QUser.user;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(blogTicketItem.id.eq(id));
        conditions.and(blog.id.eq(blogId));
        conditions.and(user.email.eq(email));

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(blogTicketItem)
                .innerJoin(blogTicketItem.blog, blog).fetchJoin()
                .innerJoin(blog.user, user).fetchJoin()
                .where(conditions)
                .fetchOne());
    }

    @Override
    public List<BlogTicketItemDto> findDetailsByBlogId(Long blogId) {
        QBlogTicketItem blogTicketItem = QBlogTicketItem.blogTicketItem;
        QReservation reservation = QReservation.reservation;
        QTicketSchedule ticketSchedule = QTicketSchedule.ticketSchedule;
        QTicket ticket = QTicket.ticket;

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
                .where(blogTicketItem.blog.id.eq(blogId))
                .fetch();
    }
}
