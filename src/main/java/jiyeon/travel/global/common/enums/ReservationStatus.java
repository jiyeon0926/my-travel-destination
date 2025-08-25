package jiyeon.travel.global.common.enums;

public enum ReservationStatus {

    UNPAID("결제 전"),
    PAID("결제 완료"),
    USED("사용 완료"),
    NO_SHOW("노쇼"),
    CANCELLED("취소");

    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }
}
