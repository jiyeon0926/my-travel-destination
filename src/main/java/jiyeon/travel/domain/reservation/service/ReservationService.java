package jiyeon.travel.domain.reservation.service;

import jiyeon.travel.domain.reservation.dto.ReservationDetailResDto;
import jiyeon.travel.domain.reservation.dto.ReservationOptionCreateReqDto;
import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.domain.reservation.entity.ReservationOption;
import jiyeon.travel.domain.reservation.repository.ReservationOptionRepository;
import jiyeon.travel.domain.reservation.repository.ReservationRepository;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketOption;
import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import jiyeon.travel.domain.ticket.repository.TicketOptionRepository;
import jiyeon.travel.domain.ticket.repository.TicketRepository;
import jiyeon.travel.domain.ticket.repository.TicketScheduleRepository;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.domain.user.repository.UserRepository;
import jiyeon.travel.global.common.enums.ReservationStatus;
import jiyeon.travel.global.common.enums.TicketSaleStatus;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationOptionRepository reservationOptionRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final TicketOptionRepository ticketOptionRepository;
    private final TicketScheduleRepository ticketScheduleRepository;

    @Transactional
    public ReservationDetailResDto createReservation(String email, Long scheduleId, Integer baseQuantity,
                                                     String reservationName, String reservationPhone,
                                                     List<ReservationOptionCreateReqDto> options) {
        User user = userRepository.findActiveByEmailOrElseThrow(email);
        TicketSchedule ticketSchedule = ticketScheduleRepository.findByIdAndIsActiveTrue(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_SCHEDULE_NOT_FOUND));
        Ticket ticket = ticketRepository.findByScheduleId(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_NOT_FOUND));

        if (ticket.isNotActiveStatus()) {
            throw new CustomException(ErrorCode.RESERVATION_ONLY_WHEN_ON_SALE);
        }

        validateBaseTicketReservation(baseQuantity, options, ticket);
        validateOptionTicketReservation(baseQuantity, options, ticket);

        return baseQuantity != null
                ? saveBaseTicketReservation(baseQuantity, reservationName, reservationPhone, ticket, user, ticketSchedule)
                : saveOptionTicketReservation(reservationName, reservationPhone, options, user, ticketSchedule);
    }

    public void paidReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findByIdWithTicketAndSchedule(reservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));

        TicketSchedule ticketSchedule = reservation.getTicketSchedule();
        ticketSchedule.decreaseRemainingQuantity(reservation.getTotalQuantity());
        reservation.changeStatus(ReservationStatus.PAID);

        Ticket ticket = ticketSchedule.getTicket();
        List<TicketSchedule> ticketSchedules = ticketScheduleRepository.findByTicketIdAndIsActiveTrue(ticket.getId());

        boolean isSoldOut = ticketSchedules.stream().allMatch(TicketSchedule::isSoldOut);
        if (isSoldOut) {
            ticket.changeSaleStatus(TicketSaleStatus.SOLD_OUT);
        }
    }

    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findByIdOrElseThrow(reservationId);
    }

    private void validateBaseTicketReservation(Integer baseQuantity, List<ReservationOptionCreateReqDto> options, Ticket ticket) {
        boolean isBaseTicket = ticket.getBasePrice() != null;

        if (isBaseTicket) {
            if (baseQuantity == null) {
                throw new CustomException(ErrorCode.BASE_TICKET_QUANTITY_REQUIRED);
            }

            if (options != null && !options.isEmpty()) {
                throw new CustomException(ErrorCode.NO_OPTION_TICKET);
            }
        }
    }

    private void validateOptionTicketReservation(Integer baseQuantity, List<ReservationOptionCreateReqDto> options, Ticket ticket) {
        boolean isOptionTicket = ticket.getBasePrice() == null;

        if (isOptionTicket) {
            if (baseQuantity != null) {
                throw new CustomException(ErrorCode.BASE_TICKET_QUANTITY_NOT_ALLOWED);
            }

            if (CollectionUtils.isEmpty(options)) {
                throw new CustomException(ErrorCode.HAS_OPTION_TICKET);
            }
        }
    }

    private ReservationDetailResDto saveBaseTicketReservation(Integer baseQuantity, String reservationName, String reservationPhone,
                                                              Ticket ticket, User user, TicketSchedule ticketSchedule) {
        int totalAmount = ticket.getBasePrice() * baseQuantity;
        String phone = reservationPhone == null ? user.getPhone() : reservationPhone;

        Reservation reservation = new Reservation(user, ticketSchedule, baseQuantity, totalAmount, reservationName, phone);
        Reservation savedReservation = reservationRepository.save(reservation);

        return new ReservationDetailResDto(savedReservation, List.of());
    }

    private ReservationDetailResDto saveOptionTicketReservation(String reservationName, String reservationPhone, List<ReservationOptionCreateReqDto> options, User user, TicketSchedule ticketSchedule) {
        long uniqueCount = options.stream()
                .map(ReservationOptionCreateReqDto::getOptionId)
                .distinct()
                .count();

        if (uniqueCount != options.size()) {
            throw new CustomException(ErrorCode.DUPLICATE_OPTION);
        }

        int totalQuantity = options.stream()
                .mapToInt(ReservationOptionCreateReqDto::getQuantity)
                .sum();

        int totalAmount = options.stream()
                .mapToInt(option -> {
                    TicketOption ticketOption = ticketOptionRepository.findByIdOrElseThrow(option.getOptionId());

                    return ticketOption.getPrice() * option.getQuantity();
                })
                .sum();

        String phone = reservationPhone == null ? user.getPhone() : reservationPhone;

        Reservation reservation = new Reservation(user, ticketSchedule, totalQuantity, totalAmount, reservationName, phone);
        Reservation savedReservation = reservationRepository.save(reservation);

        List<ReservationOption> reservationOptions = options.stream()
                .map(option -> {
                    TicketOption ticketOption = ticketOptionRepository.findByIdOrElseThrow(option.getOptionId());
                    ReservationOption reservationOption = new ReservationOption(savedReservation, ticketOption, option.getQuantity(), ticketOption.getPrice());

                    return reservationOptionRepository.save(reservationOption);
                })
                .toList();

        return new ReservationDetailResDto(savedReservation, reservationOptions);
    }
}
