package jiyeon.travel.domain.ticket.repository.custom;

import jiyeon.travel.domain.ticket.dto.TicketListResDto;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.global.common.enums.TicketSaleStatus;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomTicketRepository {

    Optional<Ticket> findByIdAndEmailWithOption(Long id, String email);

    TicketListResDto searchMyTickets(Pageable pageable, TicketSaleStatus saleStatus, String email);

    TicketListResDto searchTickets(Pageable pageable, String name);
}
