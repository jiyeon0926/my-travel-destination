package jiyeon.travel.domain.ticket.repository;

import jiyeon.travel.domain.ticket.entity.TicketImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketImageRepository extends JpaRepository<TicketImage, Long> {

    List<TicketImage> findAllByTicketId(Long ticketId);
}
