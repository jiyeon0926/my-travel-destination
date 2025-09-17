package jiyeon.travel.domain.ticket.repository.custom;

import jiyeon.travel.domain.ticket.dto.TicketListResDto;
import jiyeon.travel.domain.ticket.entity.Ticket;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomTicketRepository {

    Optional<Ticket> findByIdAndEmailWithOption(Long id, String email);

    TicketListResDto findAllByEmail(Pageable pageable, String email);
}
