package jiyeon.travel.domain.ticket.repository;

import jiyeon.travel.domain.ticket.entity.TicketSchedule;

import java.util.Optional;

public interface CustomTicketScheduleRepository {

    Optional<TicketSchedule> findByIdAndTicketIdAndEmail(Long id, Long ticketId, String email);
}
