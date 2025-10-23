package jiyeon.travel.domain.reservation.service;

import jiyeon.travel.domain.reservation.dto.ReservationDetailResDto;
import jiyeon.travel.domain.reservation.dto.ReservationOptionCreateReqDto;
import jiyeon.travel.domain.reservation.dto.ReservationSimpleResDto;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ReservationFacade {

    private final RedissonClient redissonClient;
    private final ReservationCommandService reservationCommandService;
    private final ReservationQueryService reservationQueryService;

    public ReservationDetailResDto createReservationWithLock(String email, Long scheduleId, Integer baseQuantity,
                                                             String reservationName, String reservationPhone,
                                                             List<ReservationOptionCreateReqDto> options) {
        RLock lock = redissonClient.getLock("LOCK:TICKET_SCHEDULE:" + scheduleId);
        try {
            boolean isNotLockValid = !lock.tryLock(5, 10, TimeUnit.SECONDS);
            if (isNotLockValid) {
                throw new CustomException(ErrorCode.RESERVATION_CONFLICT);
            }

            return reservationCommandService.createReservation(email, scheduleId, baseQuantity, reservationName, reservationPhone, options);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CustomException(ErrorCode.RESERVATION_INTERRUPT);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Transactional(readOnly = true)
    public ReservationDetailResDto findMyReservationById(String email, Long reservationId) {
        return reservationQueryService.findMyReservationById(email, reservationId);
    }

    @Transactional(readOnly = true)
    public List<ReservationSimpleResDto> findAll(String email, int page, int size) {
        return reservationQueryService.findAll(email, page, size);
    }

    @Transactional(readOnly = true)
    public List<ReservationSimpleResDto> findMyUsedReservations(String email) {
        return reservationQueryService.findMyUsedReservations(email);
    }
}
