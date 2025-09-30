package jiyeon.travel.domain.reservation.service;

import jiyeon.travel.domain.reservation.dto.ReservationDetailResDto;
import jiyeon.travel.domain.reservation.dto.ReservationSimpleResDto;
import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.domain.reservation.entity.ReservationOption;
import jiyeon.travel.domain.reservation.repository.ReservationRepository;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationPartnerQueryService {

    private final ReservationRepository reservationRepository;

    @Transactional(readOnly = true)
    public ReservationDetailResDto findReservationById(String email, Long reservationId) {
        Reservation reservation = reservationRepository.findByIdAndPartnerEmailWithTicketAndScheduleWithoutUnpaid(reservationId, email)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));

        TicketSchedule ticketSchedule = reservation.getTicketSchedule();
        Ticket ticket = ticketSchedule.getTicket();
        List<ReservationOption> reservationOptions = reservation.getReservationOptions();

        return new ReservationDetailResDto(reservation, ticket, ticketSchedule, reservationOptions);
    }

    @Transactional(readOnly = true)
    public List<ReservationSimpleResDto> findAllWithoutUnpaid(String email) {
        List<Reservation> reservations = reservationRepository.findAllByPartnerEmailWithoutUnpaid(email);

        return reservations.stream()
                .map(ReservationSimpleResDto::new)
                .toList();
    }
}
