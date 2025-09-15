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

    public static ReservationStatus of(String status) throws IllegalArgumentException {
        for (ReservationStatus reservationStatus : values()) {
            if (reservationStatus.name().equals(status)) {
                return reservationStatus;
            }
        }

        throw new IllegalArgumentException("해당하는 이름의 판매 상태를 찾을 수 없습니다: " + status);
    }
}
