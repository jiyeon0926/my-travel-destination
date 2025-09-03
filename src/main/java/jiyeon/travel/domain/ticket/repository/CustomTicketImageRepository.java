package jiyeon.travel.domain.ticket.repository;

import jiyeon.travel.domain.ticket.entity.TicketImage;

import java.util.Optional;

public interface CustomTicketImageRepository {

    Optional<TicketImage> findByIdAndTicketIdAndEmail(Long id, Long ticketId, String email);
}
