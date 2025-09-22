package jiyeon.travel.domain.blog.service;

import jiyeon.travel.domain.blog.dto.BlogTicketItemDto;
import jiyeon.travel.domain.blog.dto.BlogTicketItemReqDto;
import jiyeon.travel.domain.blog.entity.Blog;
import jiyeon.travel.domain.blog.entity.BlogTicketItem;
import jiyeon.travel.domain.blog.repository.BlogTicketItemRepository;
import jiyeon.travel.domain.reservation.entity.Reservation;
import jiyeon.travel.domain.reservation.service.ReservationService;
import jiyeon.travel.global.common.enums.ReservationStatus;
import jiyeon.travel.global.exception.CustomException;
import jiyeon.travel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BlogTicketItemService {

    private final BlogTicketItemRepository blogTicketItemRepository;
    private final ReservationService reservationService;

    @Transactional
    public List<BlogTicketItem> saveTicketItem(String email, Blog blog, List<BlogTicketItemReqDto> items) {
        Set<Long> checkIds = new HashSet<>();

        List<BlogTicketItem> blogTicketItems = items.stream()
                .map(item -> {
                    Long reservationId = item.getReservationId();
                    boolean isNotCheck = !checkIds.add(reservationId);
                    if (isNotCheck) {
                        throw new CustomException(ErrorCode.DUPLICATE_RESERVATION_ID);
                    }

                    Reservation reservation = reservationService.getReservationByIdAndEmailAndStatus(reservationId, email, ReservationStatus.USED);

                    return new BlogTicketItem(blog, reservation);
                })
                .toList();

        return blogTicketItemRepository.saveAll(blogTicketItems);
    }

    @Transactional
    public void addTicketItems(String email, Blog blog, Long reservationId) {
        boolean isTicketItem = blogTicketItemRepository.existsByBlogIdAndReservationId(blog.getId(), reservationId);
        if (isTicketItem) {
            throw new CustomException(ErrorCode.DUPLICATE_RESERVATION_ID);
        }

        Reservation reservation = reservationService.getReservationByIdAndEmailAndStatus(reservationId, email, ReservationStatus.USED);
        BlogTicketItem blogTicketItem = new BlogTicketItem(blog, reservation);
        blogTicketItemRepository.save(blogTicketItem);
    }

    public List<BlogTicketItemDto> findTicketItemsByBlogId(Long blogId) {
        return blogTicketItemRepository.findDetailsByBlogId(blogId);
    }
}
