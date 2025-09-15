package jiyeon.travel.domain.ticket.service;

import jiyeon.travel.domain.ticket.dto.TicketOptionCreateReqDto;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketOption;
import jiyeon.travel.domain.ticket.repository.TicketOptionRepository;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketOptionService {

    private final TicketOptionRepository ticketOptionRepository;

    @Transactional
    public List<TicketOption> createOptions(Ticket ticket, List<TicketOptionCreateReqDto> options) {
        List<TicketOption> optionList = options.stream()
                .map(option -> new TicketOption(ticket, option.getName(), option.getPrice()))
                .toList();

        return ticketOptionRepository.saveAll(optionList);
    }

    @Transactional
    public TicketOption createOption(Ticket ticket, String name, int price) {
        TicketOption ticketOption = new TicketOption(ticket, name, price);

        return ticketOptionRepository.save(ticketOption);
    }

    @Transactional
    public void deleteOption(String email, Long ticketId, Long optionId) {
        TicketOption ticketOption = ticketOptionRepository.findByIdAndTicketIdAndEmail(optionId, ticketId, email)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_OPTION_NOT_FOUND));

        if (ticketOption.isNotReadyStatus()) {
            throw new CustomException(ErrorCode.TICKET_READY_ONLY);
        }

        ticketOptionRepository.delete(ticketOption);
    }

    @Transactional
    public TicketOption updateOption(String email, Long ticketId, Long optionId, String name, Integer price) {
        TicketOption ticketOption = ticketOptionRepository.findByIdAndTicketIdAndEmail(optionId, ticketId, email)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_OPTION_NOT_FOUND));

        if (ticketOption.isNotReadyStatus()) {
            throw new CustomException(ErrorCode.TICKET_READY_ONLY);
        }

        if (name != null) ticketOption.changeName(name);
        if (price != null) ticketOption.changePrice(price);

        return ticketOption;
    }

    public List<TicketOption> findOptionsByTicketId(Long ticketId) {
        return ticketOptionRepository.findAllByTicketIdOrderByPriceAsc(ticketId);
    }
}
