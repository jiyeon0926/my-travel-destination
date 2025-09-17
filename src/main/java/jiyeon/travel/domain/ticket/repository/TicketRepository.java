package jiyeon.travel.domain.ticket.repository;

import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.repository.custom.CustomTicketRepository;
import jiyeon.travel.global.common.enums.TicketSaleStatus;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, CustomTicketRepository {

    @Query("select t from Ticket t inner join fetch t.user u where t.id = :id and u.email = :email")
    Optional<Ticket> findByIdAndEmail(Long id, String email);

    @Query("select t from Ticket t inner join TicketSchedule s on t.id = s.ticket.id where s.id = :scheduleId")
    Optional<Ticket> findByScheduleId(Long scheduleId);

    List<Ticket> findAllBySaleStatus(TicketSaleStatus saleStatus);

    default Ticket findByIdOrElseThrow(Long id) {
        return this.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_NOT_FOUND));
    }

    default Ticket findByIdAndEmailOrElseThrow(Long id, String email) {
        return this.findByIdAndEmail(id, email)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_NOT_FOUND));
    }
}
