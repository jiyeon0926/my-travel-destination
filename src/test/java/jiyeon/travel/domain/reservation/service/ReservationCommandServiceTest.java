package jiyeon.travel.domain.reservation.service;

import jiyeon.travel.domain.reservation.repository.ReservationRepository;
import jiyeon.travel.domain.ticket.dto.TicketDetailResDto;
import jiyeon.travel.domain.ticket.dto.TicketScheduleCreateReqDto;
import jiyeon.travel.domain.ticket.entity.Ticket;
import jiyeon.travel.domain.ticket.entity.TicketSchedule;
import jiyeon.travel.domain.ticket.repository.TicketRepository;
import jiyeon.travel.domain.ticket.repository.TicketScheduleRepository;
import jiyeon.travel.domain.user.entity.User;
import jiyeon.travel.domain.user.repository.UserRepository;
import jiyeon.travel.global.common.enums.TicketSaleStatus;
import jiyeon.travel.global.common.enums.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
class ReservationCommandServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketScheduleRepository ticketScheduleRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TicketPartnerCommandFacade ticketPartnerCommandFacade;

    @Autowired
    private ReservationFacade reservationFacade;

    private User partner;

    @BeforeEach
    void setUp() {
        String email = "partner@naver.com";
        partner = userRepository.save(
                new User(email, "1234", "토끼", "010", UserRole.PARTNER)
        );
    }

    @AfterEach
    void tearDown() {
        reservationRepository.deleteAllInBatch();
        ticketScheduleRepository.deleteAllInBatch();
        ticketRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("티켓 일정 재고가 10개일 때, 동시에 15명이 수량 1개씩 예약 요청을 합니다.")
    @Test
    void reservationConcurrencyTest() throws InterruptedException {
        // given
        Long scheduleId = createTicket().getSchedules().get(0).getScheduleId();
        TicketSchedule ticketSchedule = ticketScheduleRepository.findById(scheduleId).orElseThrow();

        Ticket ticket = ticketRepository.findByScheduleId(scheduleId).orElseThrow();
        ticket.changeSaleStatus(TicketSaleStatus.ACTIVE);
        ticketRepository.save(ticket);

        int threadCount = 15;
        int initStock = ticketSchedule.getRemainingQuantity();

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        List<User> users = getUsers(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            int index = i;
            executorService.execute(() -> {
                try {
                    reservationFacade.createReservationWithLock(
                            users.get(index).getEmail(),
                            ticketSchedule.getId(),
                            1,
                            "예약자" + index,
                            "010",
                            Collections.emptyList()
                    );

                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.err.println("예약 실패: " + e.getMessage());
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        TicketSchedule finalSchedule = ticketScheduleRepository.findById(ticketSchedule.getId())
                .orElseThrow();

        System.out.println("=== 동시성 테스트 결과 ===");
        System.out.println("초기 재고: " + initStock);
        System.out.println("성공한 예약: " + successCount.get());
        System.out.println("실패한 예약: " + failCount.get());
        System.out.println("최종 재고: " + finalSchedule.getRemainingQuantity());
        System.out.println("총 처리된 요청: " + (successCount.get() + failCount.get()));

        assertEquals(threadCount, successCount.get() + failCount.get(), "전체 요청 수가 일치해야 합니다.");
        assertEquals(finalSchedule.getRemainingQuantity(), initStock - successCount.get(), "최종 재고가 예약된 수량만큼 차감되지 않았습니다.");
    }

    private TicketDetailResDto createTicket() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime saleStartDate = now.plusSeconds(1);
        LocalDateTime saleEndDate = now.plusDays(1);

        List<TicketScheduleCreateReqDto> schedules = List.of(
                new TicketScheduleCreateReqDto(saleStartDate.toLocalDate(), null, 10)
        );

        return ticketPartnerCommandFacade.createTicket(
                partner.getEmail(),
                "타르트 만들기 체험",
                saleStartDate,
                saleEndDate,
                "010",
                "경기도",
                10000,
                "설명",
                null,
                schedules,
                null
        );
    }

    private List<User> getUsers(int threadCount) {
        return IntStream.range(0, threadCount)
                .mapToObj(i -> {
                    User user = createUser(
                            "user" + i + "@naver.com",
                            "1234",
                            "사용자" + i,
                            "010"
                    );

                    return userRepository.save(user);
                })
                .toList();
    }

    private User createUser(String email, String password, String displayName, String phone) {
        return new User(email, password, displayName, phone, UserRole.USER);
    }
}