package jiyeon.travel.global.common.enums;

public enum TicketSaleStatus {

    READY("판매 전"),
    ACTIVE("판매중"),
    INACTIVE("판매 중지"),
    CLOSED("판매 종료"),
    SOLD_OUT("매진");

    private final String description;

    TicketSaleStatus(String description) {
        this.description = description;
    }

    public static TicketSaleStatus of(String status) throws IllegalArgumentException {
        for (TicketSaleStatus saleStatus : values()) {
            if (saleStatus.name().equals(status)) {
                return saleStatus;
            }
        }

        throw new IllegalArgumentException("해당하는 이름의 판매 상태를 찾을 수 없습니다: " + status);
    }
}
