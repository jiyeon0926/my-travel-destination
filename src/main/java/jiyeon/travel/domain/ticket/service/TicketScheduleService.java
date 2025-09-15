package jiyeon.travel.domain.ticket.service;

import jiyeon.travel.domain.ticket.dto.TicketScheduleCreateReqDto;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import jiyeon.travel.domain.ticket.repository.TicketScheduleRepository;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketScheduleService {

    private final TicketScheduleRepository ticketScheduleRepository;

    @Transactional
    public List<TicketSchedule> createSchedules(Ticket ticket, List<TicketScheduleCreateReqDto> schedules) {
        validateNullTimeSchedules(schedules);

        List<TicketSchedule> scheduleList = schedules.stream()
                .map(schedule -> {
                    LocalDate startDate = schedule.getStartDate();
                    LocalTime startTime = schedule.getStartTime();
                    validateScheduleBySaleDateTime(ticket, startDate, startTime);

                    return new TicketSchedule(ticket, schedule.getStartDate(), schedule.getStartTime(), schedule.getQuantity());
                })
                .toList();

        return ticketScheduleRepository.saveAll(scheduleList);
    }

    @Transactional
    public TicketSchedule createSchedule(Ticket ticket, LocalDate startDate, LocalTime startTime, int quantity) {
        validateDuplicateDateAndTime(ticket.getId(), startDate, startTime);
        validateScheduleBySaleDateTime(ticket, startDate, startTime);

        TicketSchedule ticketSchedule = new TicketSchedule(ticket, startDate, startTime, quantity);

        return ticketScheduleRepository.save(ticketSchedule);
    }

    @Transactional
    public void deleteSchedule(String email, Long ticketId, Long scheduleId) {
        TicketSchedule ticketSchedule = ticketScheduleRepository.findByIdAndTicketIdAndEmail(scheduleId, ticketId, email)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_SCHEDULE_NOT_FOUND));

        if (ticketSchedule.isNotReadyStatus()) {
            throw new CustomException(ErrorCode.TICKET_READY_ONLY);
        }

        ticketScheduleRepository.delete(ticketSchedule);
    }

    @Transactional
    public TicketSchedule updateSchedule(String email, Long ticketId, Long scheduleId, LocalDate startDate,
                                             LocalTime startTime, Boolean isActive, Integer quantity) {
        TicketSchedule ticketSchedule = ticketScheduleRepository.findByIdAndTicketIdAndEmail(scheduleId, ticketId, email)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_SCHEDULE_NOT_FOUND));

        if (ticketSchedule.isNotReadyStatus()) {
            throw new CustomException(ErrorCode.TICKET_READY_ONLY);
        }

        validateDuplicateDateAndTime(ticketId, startDate, startTime);
        validateScheduleBySaleDateTime(ticketSchedule.getTicket(), startDate, startTime);

        if (isActive != null) ticketSchedule.changeIsActive(isActive);
        if (startDate != null) ticketSchedule.changeStartDate(startDate);
        if (startTime != null) ticketSchedule.changeStartTime(startTime);
        if (quantity != null) ticketSchedule.increaseQuantity(quantity);

        return ticketSchedule;
    }

    public List<TicketSchedule> findActiveSchedulesByTicketId(Long ticketId) {
        return ticketScheduleRepository.findAllByTicketIdAndIsActiveTrueOrderByStartDateAsc(ticketId);
    }

    private void validateNullTimeSchedules(List<TicketScheduleCreateReqDto> schedules) {
        schedules.stream()
                .collect(Collectors.groupingBy(TicketScheduleCreateReqDto::getStartDate))
                .forEach((date, shceduleReqList) -> {
                    boolean hasAllDaySchedule = shceduleReqList.stream()
                            .anyMatch(schedule -> schedule.getStartTime() == null);

                    boolean hasTimedSchedule = shceduleReqList.stream()
                            .anyMatch(schedule -> schedule.getStartTime() != null);

                    if (hasAllDaySchedule && hasTimedSchedule) {
                        throw new CustomException(ErrorCode.NULL_TIME_SCHEDULE_DUPLICATE);
                    }
                });
    }

    private void validateDuplicateDateAndTime(Long ticketId, LocalDate startDate, LocalTime startTime) {
        boolean isDate = ticketScheduleRepository.existsByTicketIdAndStartDate(ticketId, startDate);
        boolean isDateAndTime = ticketScheduleRepository.existsByTicketIdAndStartDateAndStartTime(ticketId, startDate, startTime);
        boolean isNullTime = ticketScheduleRepository.existsByTicketIdAndStartDateAndStartTimeIsNull(ticketId, startDate);

        if ((isDate && startTime == null) || isDateAndTime) {
            throw new CustomException(ErrorCode.TICKET_SCHEDULE_ALREADY_EXISTS);
        }

        if ((isNullTime && isDate)) {
            throw new CustomException(ErrorCode.NULL_TIME_SCHEDULE_DUPLICATE);
        }
    }

    private void validateScheduleBySaleDateTime(Ticket ticket, LocalDate startDate, LocalTime startTime) {
        LocalDate saleStartDate = ticket.getSaleStartDate().toLocalDate();
        LocalDate saleEndDate = ticket.getSaleEndDate().toLocalDate();
        LocalTime saleStartTime = ticket.getSaleStartDate().toLocalTime();
        LocalTime saleEndTime = ticket.getSaleEndDate().toLocalTime();

        if (startDate.isBefore(saleStartDate) || startDate.isAfter(saleEndDate)) {
            throw new CustomException(ErrorCode.SCHEDULE_OUT_OF_SALE_RANGE);
        }

        if (startTime != null) {
            if (startDate.isEqual(saleStartDate) && !startTime.isAfter(saleStartTime)
                    || startDate.isEqual(saleEndDate) && !startTime.isBefore(saleEndTime)) {
                throw new CustomException(ErrorCode.SCHEDULE_OUT_OF_SALE_RANGE);
            }
        }
    }
}
