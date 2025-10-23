package jiyeon.travel.domain.blog.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jiyeon.travel.domain.blog.dto.BlogListResDto;
import jiyeon.travel.domain.blog.dto.BlogPreviewResDto;
import jiyeon.travel.domain.blog.dto.QBlogPreviewResDto;
import jiyeon.travel.domain.blog.entity.QBlog;
import jiyeon.travel.domain.blog.entity.QBlogImage;
import jiyeon.travel.domain.blog.entity.QBlogTicketItem;
import jiyeon.travel.domain.reservation.entity.QReservation;
import jiyeon.travel.domain.ticket.dto.QTicketBlogDto;
import jiyeon.travel.domain.ticket.dto.TicketBlogDto;
import jiyeon.travel.domain.ticket.entity.QTicket;
import jiyeon.travel.domain.ticket.entity.QTicketSchedule;
import jiyeon.travel.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomBlogRepositoryImpl implements CustomBlogRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public BlogListResDto findAllMyBlogs(String email) {
        QBlog blog = QBlog.blog;
        QBlogImage blogImage = QBlogImage.blogImage;
        QUser user = QUser.user;

        Long total = Optional.ofNullable(
                        jpaQueryFactory
                                .select(blog.count())
                                .from(blog)
                                .innerJoin(user).on(blog.user.id.eq(user.id))
                                .where(user.email.eq(email))
                                .fetchOne())
                .orElse(0L);

        List<BlogPreviewResDto> blogs = jpaQueryFactory
                .select(new QBlogPreviewResDto(
                        blog.id,
                        blog.title,
                        blogImage.imageUrl,
                        blogImage.fileName,
                        blogImage.isMain
                ))
                .from(blog)
                .leftJoin(blogImage).on(blog.id.eq(blogImage.blog.id).and(blogImage.isMain.eq(true)))
                .innerJoin(user).on(blog.user.id.eq(user.id))
                .where(user.email.eq(email))
                .fetch();

        return new BlogListResDto(total, blogs);
    }

    @Override
    public BlogListResDto searchBlogs(Pageable pageable, String title, LocalDate travelStartDate, LocalDate travelEndDate, Integer totalExpense) {
        QBlog blog = QBlog.blog;
        QBlogImage blogImage = QBlogImage.blogImage;

        BooleanBuilder conditions = new BooleanBuilder();
        if (title != null) conditions.and(blog.title.contains(title));
        if (travelStartDate != null) conditions.and(blog.travelStartDate.eq(travelStartDate));
        if (travelEndDate != null) conditions.and(blog.travelEndDate.eq(travelEndDate));
        if (totalExpense != null) conditions.and(blog.totalExpense.eq(totalExpense));

        Long total = Optional.ofNullable(
                        jpaQueryFactory
                                .select(blog.count())
                                .from(blog)
                                .where(conditions)
                                .fetchOne())
                .orElse(0L);

        List<BlogPreviewResDto> blogs = jpaQueryFactory
                .select(new QBlogPreviewResDto(
                        blog.id,
                        blog.title,
                        blogImage.imageUrl,
                        blogImage.fileName,
                        blogImage.isMain
                ))
                .from(blog)
                .leftJoin(blogImage).on(blog.id.eq(blogImage.blog.id).and(blogImage.isMain.eq(true)))
                .where(conditions)
                .orderBy(blog.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new BlogListResDto(total, blogs);
    }

    @Override
    public List<TicketBlogDto> findAllByTicketId(Pageable pageable, Long ticketId) {
        QBlog blog = QBlog.blog;
        QBlogTicketItem blogTicketItem = QBlogTicketItem.blogTicketItem;
        QReservation reservation = QReservation.reservation;
        QTicketSchedule ticketSchedule = QTicketSchedule.ticketSchedule;
        QTicket ticket = QTicket.ticket;

        return jpaQueryFactory
                .select(new QTicketBlogDto(
                        blog.id,
                        blog.title,
                        blog.createdAt
                ))
                .from(blog)
                .innerJoin(blogTicketItem).on(blog.id.eq(blogTicketItem.blog.id))
                .innerJoin(reservation).on(blogTicketItem.reservation.id.eq(reservation.id))
                .innerJoin(ticketSchedule).on(reservation.ticketSchedule.id.eq(ticketSchedule.id))
                .innerJoin(ticket).on(ticketSchedule.ticket.id.eq(ticket.id))
                .where(ticket.id.eq(ticketId))
                .groupBy(blog.id)
                .orderBy(blog.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
