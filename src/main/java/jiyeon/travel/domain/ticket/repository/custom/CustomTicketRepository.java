package jiyeon.travel.domain.ticket.repository.custom;

import jiyeon.travel.domain.ticket.dto.TicketListResDto;
import jiyeon.travel.domain.ticket.entity.Ticket;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomTicketRepository {

    Optional<Ticket> findByIdAndEmailWithUserAndOption(Long id, String email);

    Optional<Ticket> findByIdAndEmail(Long id, String email);

    TicketListResDto findAllByEmail(Pageable pageable, String email);
}
