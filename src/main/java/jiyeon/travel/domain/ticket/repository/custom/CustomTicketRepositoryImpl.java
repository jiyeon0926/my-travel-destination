package jiyeon.travel.domain.ticket.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jiyeon.travel.domain.ticket.dto.QTicketPreviewResDto;
import jiyeon.travel.domain.ticket.dto.TicketListResDto;
import jiyeon.travel.domain.ticket.dto.TicketPreviewResDto;
import jiyeon.travel.domain.ticket.entity.QTicket;
import jiyeon.travel.domain.ticket.entity.QTicketImage;
import jiyeon.travel.domain.ticket.entity.QTicketOption;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.user.entity.QUser;
import jiyeon.travel.global.common.enums.TicketSaleStatus;
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
    public Optional<Ticket> findActiveTicketByIdWithOptionAndImage(Long id) {
        QTicket ticket = QTicket.ticket;
        QTicketOption ticketOption = QTicketOption.ticketOption;
        QTicketImage ticketImage = QTicketImage.ticketImage;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(ticket.id.eq(id));
        conditions.and(ticket.saleStatus.eq(TicketSaleStatus.ACTIVE));

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(ticket)
                .innerJoin(ticket.ticketOptions, ticketOption).fetchJoin()
                .innerJoin(ticket.ticketImages, ticketImage).fetchJoin()
                .where(conditions)
                .fetchOne());
    }

    @Override
    public TicketListResDto searchMyTickets(Pageable pageable, TicketSaleStatus saleStatus, String email) {
        QUser user = QUser.user;
        QTicket ticket = QTicket.ticket;
        QTicketImage ticketImage = QTicketImage.ticketImage;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(user.email.eq(email));
        if (saleStatus != null) conditions.and(ticket.saleStatus.eq(saleStatus));

        Long total = Optional.ofNullable(
                        jpaQueryFactory
                                .select(ticket.count())
                                .from(ticket)
                                .innerJoin(user).on(ticket.user.id.eq(user.id))
                                .where(conditions)
                                .fetchOne())
                .orElse(0L);

        List<TicketPreviewResDto> tickets = jpaQueryFactory
                .select(new QTicketPreviewResDto(
                        ticket.id,
                        ticket.name,
                        ticket.saleStatus.stringValue(),
                        ticketImage.imageUrl,
                        ticketImage.fileName,
                        ticketImage.isMain
                ))
                .from(ticket)
                .leftJoin(ticketImage).on(ticket.id.eq(ticketImage.ticket.id).and(ticketImage.isMain.eq(true)))
                .innerJoin(user).on(ticket.user.id.eq(user.id))
                .where(conditions)
                .orderBy(ticket.saleStartDate.desc(), ticket.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new TicketListResDto(total, tickets);
    }

    @Override
    public TicketListResDto searchTickets(Pageable pageable, String name) {
        QTicket ticket = QTicket.ticket;
        QTicketImage ticketImage = QTicketImage.ticketImage;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(ticket.saleStatus.eq(TicketSaleStatus.ACTIVE));
        if (name != null) conditions.and(ticket.name.contains(name));

        Long total = Optional.ofNullable(
                        jpaQueryFactory
                                .select(ticket.count())
                                .from(ticket)
                                .where(conditions)
                                .fetchOne())
                .orElse(0L);

        List<TicketPreviewResDto> tickets = jpaQueryFactory
                .select(new QTicketPreviewResDto(
                        ticket.id,
                        ticket.name,
                        ticket.saleStatus.stringValue(),
                        ticketImage.imageUrl,
                        ticketImage.fileName,
                        ticketImage.isMain
                ))
                .from(ticket)
                .leftJoin(ticketImage).on(ticket.id.eq(ticketImage.ticket.id).and(ticketImage.isMain.eq(true)))
                .where(conditions)
                .orderBy(ticket.saleStartDate.desc(), ticket.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new TicketListResDto(total, tickets);
    }
}
