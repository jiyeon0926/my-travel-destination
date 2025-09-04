package jiyeon.travel.domain.ticket.repository;

import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import jiyeon.travel.domain.ticket.repository.custom.CustomTicketScheduleRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface TicketScheduleRepository extends JpaRepository<TicketSchedule, Long>, CustomTicketScheduleRepository {

    boolean existsByTicketIdAndStartDate(Long ticketId, LocalDate startDate);

    boolean existsByTicketIdAndStartDateAndStartTime(Long ticketId, LocalDate startDate, LocalTime startTime);
}
