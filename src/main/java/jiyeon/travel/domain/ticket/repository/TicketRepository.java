package jiyeon.travel.domain.ticket.repository;

import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.repository.custom.CustomTicketRepository;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, CustomTicketRepository {

    default Ticket findByIdOrElseThrow(Long id) {
        return this.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_NOT_FOUND));
    }

    default Ticket findByIdAndEmailOrElseThrow(Long id, String email) {
        return this.findByIdAndEmail(id, email)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_NOT_FOUND));
    }
}
