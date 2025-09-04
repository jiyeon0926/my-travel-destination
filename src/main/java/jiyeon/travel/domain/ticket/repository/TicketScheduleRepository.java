package jiyeon.travel.domain.ticket.repository;

import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface TicketScheduleRepository extends JpaRepository<TicketSchedule, Long> {

    boolean existsByTicketIdAndStartDate(Long ticketId, LocalDate startDate);
    boolean existsByTicketIdAndStartDateAndStartTime(Long ticketId, LocalDate startDate, LocalTime startTime);
}
