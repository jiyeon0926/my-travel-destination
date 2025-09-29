package jiyeon.travel.domain.reservation.service;

import jiyeon.travel.domain.reservation.dto.ReservationDetailResDto;
import jiyeon.travel.domain.reservation.dto.ReservationOptionCreateReqDto;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ReservationFacadeService {

    private final RedissonClient redissonClient;
    private final ReservationService reservationService;

    public ReservationDetailResDto createReservationWithLock(String email, Long scheduleId, Integer baseQuantity,
                                                             String reservationName, String reservationPhone,
                                                             List<ReservationOptionCreateReqDto> options) {
        RLock lock = redissonClient.getLock("LOCK:TICKET_SCHEDULE:" + scheduleId);
        try {
            boolean isNotLockValid = !lock.tryLock(5, 10, TimeUnit.SECONDS);
            if (isNotLockValid) {
                throw new CustomException(ErrorCode.RESERVATION_CONFLICT);
            }

            return reservationService.createReservation(email, scheduleId, baseQuantity, reservationName, reservationPhone, options);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CustomException(ErrorCode.RESERVATION_INTERRUPT);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
