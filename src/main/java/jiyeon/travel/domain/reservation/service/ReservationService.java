package jiyeon.travel.domain.reservation.service;

import jiyeon.travel.domain.reservation.dto.ReservationDetailResDto;
import jiyeon.travel.domain.reservation.dto.ReservationOptionCreateReqDto;
import jiyeon.travel.domain.reservation.dto.ReservationSimpleResDto;
import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.domain.reservation.entity.ReservationOption;
import jiyeon.travel.domain.reservation.repository.ReservationOptionRepository;
import jiyeon.travel.domain.reservation.repository.ReservationRepository;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketOption;
import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import jiyeon.travel.domain.ticket.service.TicketQueryService;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.domain.user.service.UserService;
import jiyeon.travel.global.common.enums.ReservationStatus;
import jiyeon.travel.global.common.enums.TicketSaleStatus;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationOptionRepository reservationOptionRepository;
    private final UserService userService;
    private final TicketQueryService ticketQueryService;

    @Transactional
    public ReservationDetailResDto createReservation(String email, Long scheduleId, Integer baseQuantity,
                                                     String reservationName, String reservationPhone,
                                                     List<ReservationOptionCreateReqDto> options) {
        User user = userService.getActiveUserByEmail(email);
        TicketSchedule ticketSchedule = ticketQueryService.getActiveSchedule(scheduleId);
        Ticket ticket = ticketQueryService.getTicketByScheduleId(scheduleId);

        if (ticket.isNotActiveStatus()) {
            throw new CustomException(ErrorCode.RESERVATION_ONLY_WHEN_ON_SALE);
        }

        validateBaseTicketReservation(baseQuantity, options, ticket);
        validateOptionTicketReservation(baseQuantity, options, ticket);

        int totalQuantity = calculateTotalQuantity(baseQuantity, options);
        ticketSchedule.decreaseRemainingQuantity(totalQuantity);

        return (baseQuantity != null)
                ? saveBaseTicketReservation(totalQuantity, reservationName, reservationPhone, ticket, user, ticketSchedule)
                : saveOptionTicketReservation(totalQuantity, reservationName, reservationPhone, options, ticket, user, ticketSchedule);
    }

    @Transactional(readOnly = true)
    public List<ReservationSimpleResDto> findAll(String email) {
        List<Reservation> reservations = reservationRepository.findAllByEmail(email);

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

    @Transactional(readOnly = true)
    public ReservationDetailResDto findMyReservationById(String email, Long reservationId) {
        Reservation reservation = reservationRepository.findByIdAndUserEmailWithSchedule(reservationId, email)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));

        List<ReservationOption> reservationOptions = reservation.getReservationOptions();
        TicketSchedule ticketSchedule = reservation.getTicketSchedule();
        Ticket ticket = ticketSchedule.getTicket();

        return new ReservationDetailResDto(reservation, ticket, ticketSchedule, reservationOptions);
    }

    @Transactional
    public void confirmReservationPayment(Long reservationId) {
        Reservation reservation = reservationRepository.findByIdWithTicketAndSchedule(reservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));

        TicketSchedule ticketSchedule = reservation.getTicketSchedule();
        ticketSchedule.decreaseRemainingQuantity(reservation.getTotalQuantity());
        reservation.changeStatus(ReservationStatus.PAID);

        Ticket ticket = ticketSchedule.getTicket();
        List<TicketSchedule> ticketSchedules = ticketQueryService.findActiveSchedulesByTicketId(ticket.getId());

        boolean isSoldOut = ticketSchedules.stream().allMatch(TicketSchedule::isSoldOut);
        if (isSoldOut) {
            ticket.changeSaleStatus(TicketSaleStatus.SOLD_OUT);
        }
    }

    @Transactional
    public void expireReservations() {
        List<Reservation> reservations = reservationRepository.findAllByStatusWithSchedule(ReservationStatus.UNPAID);
        LocalDateTime now = LocalDateTime.now();

        reservations.stream()
                .filter(reservation -> reservation.getCreatedAt().plusMinutes(30).isBefore(now))
                .forEach(reservation -> {
                    reservation.changeStatus(ReservationStatus.EXPIRED);

                    TicketSchedule ticketSchedule = reservation.getTicketSchedule();
                    ticketSchedule.increaseRemainingQuantity(reservation.getTotalQuantity());
                });
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

    private int calculateTotalQuantity(Integer baseQuantity, List<ReservationOptionCreateReqDto> options) {
        return baseQuantity != null
                ? baseQuantity
                : options.stream()
                .mapToInt(ReservationOptionCreateReqDto::getQuantity)
                .sum();
    }

    private ReservationDetailResDto saveBaseTicketReservation(int totalQuantity, String reservationName, String reservationPhone,
                                                              Ticket ticket, User user, TicketSchedule ticketSchedule) {
        int totalAmount = ticket.getBasePrice() * totalQuantity;
        String phone = reservationPhone == null ? user.getPhone() : reservationPhone;

        Reservation reservation = new Reservation(user, ticketSchedule, totalQuantity, totalAmount, reservationName, phone);
        Reservation savedReservation = reservationRepository.save(reservation);

        return new ReservationDetailResDto(savedReservation, ticket, ticketSchedule, List.of());
    }

    private ReservationDetailResDto saveOptionTicketReservation(int totalQuantity, String reservationName, String reservationPhone, List<ReservationOptionCreateReqDto> options,
                                                                Ticket ticket, User user, TicketSchedule ticketSchedule) {
        validateDuplicateOptions(options);

        int totalAmount = options.stream()
                .mapToInt(option -> {
                    TicketOption ticketOption = ticketQueryService.getOptionById(option.getOptionId());

                    return ticketOption.getPrice() * option.getQuantity();
                })
                .sum();

        String phone = reservationPhone == null ? user.getPhone() : reservationPhone;

        Reservation reservation = new Reservation(user, ticketSchedule, totalQuantity, totalAmount, reservationName, phone);
        Reservation savedReservation = reservationRepository.save(reservation);

        List<ReservationOption> reservationOptions = options.stream()
                .map(option -> {
                    TicketOption ticketOption = ticketQueryService.getOptionById(option.getOptionId());
                    ReservationOption reservationOption = new ReservationOption(savedReservation, ticketOption, option.getQuantity(), ticketOption.getPrice());

                    return reservationOptionRepository.save(reservationOption);
                })
                .toList();

        return new ReservationDetailResDto(savedReservation, ticket, ticketSchedule, reservationOptions);
    }

    private void validateDuplicateOptions(List<ReservationOptionCreateReqDto> options) {
        long uniqueCount = options.stream()
                .map(ReservationOptionCreateReqDto::getOptionId)
                .distinct()
                .count();

        if (uniqueCount != options.size()) {
            throw new CustomException(ErrorCode.DUPLICATE_OPTION);
        }
    }
}
