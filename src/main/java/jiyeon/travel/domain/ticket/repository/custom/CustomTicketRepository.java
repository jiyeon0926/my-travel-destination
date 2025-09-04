package jiyeon.travel.domain.ticket.repository.custom;

import jiyeon.travel.domain.ticket.entity.Ticket;

import java.util.Optional;

public interface CustomTicketRepository {

    Optional<Ticket> findByIdAndEmailWithUserAndOption(Long id, String email);

    Optional<Ticket> findByIdAndEmail(Long id, String email);
}
