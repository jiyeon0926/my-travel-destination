package jiyeon.travel.domain.ticket.repository;

import jiyeon.travel.domain.ticket.entity.TicketOption;
import jiyeon.travel.domain.ticket.repository.custom.CustomTicketOptionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketOptionRepository extends JpaRepository<TicketOption, Long>, CustomTicketOptionRepository {
}
