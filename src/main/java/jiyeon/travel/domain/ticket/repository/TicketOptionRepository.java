package jiyeon.travel.domain.ticket.repository;

import jiyeon.travel.domain.ticket.entity.TicketOption;
import jiyeon.travel.domain.ticket.repository.custom.CustomTicketOptionRepository;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketOptionRepository extends JpaRepository<TicketOption, Long>, CustomTicketOptionRepository {

    default TicketOption findByIdOrElseThrow(Long id) {
        return this.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_OPTION_NOT_FOUND));
    }

    List<TicketOption> findAllByTicketIdOrderByPriceAsc(Long ticketId);
}
