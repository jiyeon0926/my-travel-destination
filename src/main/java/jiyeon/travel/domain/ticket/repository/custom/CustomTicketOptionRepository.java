package jiyeon.travel.domain.ticket.repository.custom;

import jiyeon.travel.domain.ticket.entity.TicketOption;

import java.util.Optional;

public interface CustomTicketOptionRepository {

    Optional<TicketOption> findByIdAndTicketIdAndEmail(Long id, Long ticketId, String email);
}
