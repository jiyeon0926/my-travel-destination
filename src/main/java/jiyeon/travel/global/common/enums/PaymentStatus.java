package jiyeon.travel.global.common.enums;

public enum PaymentStatus {

    READY("결제 준비"),
    COMPLETED("결제 완료"),
    FAILED("결제 실패"),
    REFUNDED("환불 완료");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }
}
