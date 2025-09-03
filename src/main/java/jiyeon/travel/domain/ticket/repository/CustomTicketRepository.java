package jiyeon.travel.domain.ticket.repository;

import jiyeon.travel.domain.ticket.entity.Ticket;

import java.util.Optional;

public interface CustomTicketRepository {

    Optional<Ticket> findByIdAndEmailWithUserAndOption(Long id, String email);
}
