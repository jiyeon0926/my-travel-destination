package jiyeon.travel.domain.ticket.repository;

import jiyeon.travel.domain.ticket.entity.TicketImage;
import jiyeon.travel.domain.ticket.repository.custom.CustomTicketImageRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketImageRepository extends JpaRepository<TicketImage, Long>, CustomTicketImageRepository {

    Optional<TicketImage> findByTicketIdAndIsMainTrue(Long ticketId);

    List<TicketImage> findAllByTicketId(Long ticketId);

    int countByTicketId(Long ticketId);
}
