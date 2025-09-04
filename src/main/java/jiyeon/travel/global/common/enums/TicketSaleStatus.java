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
}
