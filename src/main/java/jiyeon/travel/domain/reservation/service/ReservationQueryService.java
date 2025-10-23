package jiyeon.travel.domain.reservation.service;

import jiyeon.travel.domain.reservation.dto.ReservationDetailResDto;
import jiyeon.travel.domain.reservation.dto.ReservationSimpleResDto;
import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.domain.reservation.entity.ReservationOption;
import jiyeon.travel.domain.reservation.repository.ReservationRepository;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import jiyeon.travel.global.common.enums.ReservationStatus;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationQueryService {

    private final ReservationRepository reservationRepository;

    @Transactional(readOnly = true)
    public ReservationDetailResDto findMyReservationById(String email, Long reservationId) {
        Reservation reservation = reservationRepository.findByIdAndUserEmailWithSchedule(reservationId, email)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));

        List<ReservationOption> reservationOptions = reservation.getReservationOptions();
        TicketSchedule ticketSchedule = reservation.getTicketSchedule();
        Ticket ticket = ticketSchedule.getTicket();

        return new ReservationDetailResDto(reservation, ticket, ticketSchedule, reservationOptions);
    }

    @Transactional(readOnly = true)
    public List<ReservationSimpleResDto> findAll(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        List<Reservation> reservations = reservationRepository.findAllByEmail(email, pageable);

        return reservations.stream()
                .map(ReservationSimpleResDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservationSimpleResDto> findMyUsedReservations(String email) {
        List<Reservation> reservations = reservationRepository.findAllByEmailAndStatus(email, ReservationStatus.USED);

        return reservations.stream()
                .map(ReservationSimpleResDto::new)
                .toList();
    }

    public Reservation getReservationByIdWithTicketAndSchedule(Long reservationId) {
        return reservationRepository.findByIdWithTicketAndSchedule(reservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    public Reservation getReservationByIdAndEmailAndStatus(Long reservationId, String email, ReservationStatus status) {
        return reservationRepository.findByIdAndEmailAndStatus(reservationId, email, status)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    public List<Reservation> findReservationsByTicketIdWithTicketAndSchedule(Long ticketId) {
        return reservationRepository.findAllByTicketIdWithTicketAndSchedule(ticketId);
    }
}
