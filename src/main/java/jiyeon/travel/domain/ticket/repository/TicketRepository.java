package jiyeon.travel.domain.ticket.repository;

import jiyeon.travel.domain.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, CustomTicketRepository {

    Optional<Ticket> findByIdAndUserId(Long id, Long userId);
}
