package jiyeon.travel.domain.reservation.service;

import jiyeon.travel.domain.reservation.dto.ReservationSimpleResDto;
import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.domain.reservation.repository.ReservationRepository;
import jiyeon.travel.global.common.enums.ReservationStatus;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationPartnerService {

    private final ReservationRepository reservationRepository;

    @Transactional
    public ReservationSimpleResDto changeReservationStatusById(String email, Long reservationId, String status) {
        Reservation reservation = reservationRepository.findByIdAndEmailWithTicketAndSchedule(reservationId, email)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));

        if (reservation.isNotPaidStatus()) {
            throw new CustomException(ErrorCode.INVALID_STATUS_CHANGE);
        }

        ReservationStatus currentStatus = ReservationStatus.of(status);
        if (reservation.isNotUsedOrNoShow(currentStatus)) {
            throw new CustomException(ErrorCode.INVALID_RESERVATION_STATUS_CHANGE);
        }

        reservation.changeStatus(currentStatus);

        return new ReservationSimpleResDto(reservation);
    }
}
