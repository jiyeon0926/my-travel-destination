package jiyeon.travel.domain.ticket.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TicketTest {

    @DisplayName("판매 시작일이 현재 시간과 같거나 이후면 참입니다.")
    @Test
    void hasSaleStarted() {
        // given
        LocalDateTime saleStartDate = LocalDateTime.of(2025, 9, 10, 10, 0, 0);

        Ticket ticket = Ticket.builder()
                .saleStartDate(saleStartDate)
                .build();

        // when
        LocalDateTime referenceTime1 = LocalDateTime.of(2025, 9, 10, 9, 59, 0);
        LocalDateTime referenceTime2 = LocalDateTime.of(2025, 9, 10, 10, 0, 0);
        LocalDateTime referenceTime3 = LocalDateTime.of(2025, 9, 10, 10, 1, 0);

        boolean hasSaleStarted1 = ticket.hasSaleStarted(referenceTime1);
        boolean hasSaleStarted2 = ticket.hasSaleStarted(referenceTime2);
        boolean hasSaleStarted3 = ticket.hasSaleStarted(referenceTime3);

        // then
        assertThat(hasSaleStarted1).isFalse();
        assertThat(hasSaleStarted2).isTrue();
        assertThat(hasSaleStarted3).isTrue();
    }

    @DisplayName("판매 종료일이 현재 시간과 같거나 이후면 참입니다.")
    @Test
    void hasSaleEnded() {
        // given
        LocalDateTime saleEndDate = LocalDateTime.of(2025, 9, 11, 18, 0, 0);

        Ticket ticket = Ticket.builder()
                .saleEndDate(saleEndDate)
                .build();

        // when
        LocalDateTime referenceTime1 = LocalDateTime.of(2025, 9, 11, 17, 59, 0);
        LocalDateTime referenceTime2 = LocalDateTime.of(2025, 9, 11, 18, 0, 0);
        LocalDateTime referenceTime3 = LocalDateTime.of(2025, 9, 11, 18, 1, 0);

        boolean hasSaleEnded1 = ticket.hasSaleEnded(referenceTime1);
        boolean hasSaleEnded2 = ticket.hasSaleEnded(referenceTime2);
        boolean hasSaleEnded3 = ticket.hasSaleEnded(referenceTime3);

        // then
        assertThat(hasSaleEnded1).isFalse();
        assertThat(hasSaleEnded2).isTrue();
        assertThat(hasSaleEnded3).isTrue();
    }
}