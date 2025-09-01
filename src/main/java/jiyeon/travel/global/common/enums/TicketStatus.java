package jiyeon.travel.global.common.enums;

public enum TicketStatus {

    READY("판매 전"),
    ACTIVE("판매중"),
    INACTIVE("판매 중지"),
    CLOSED("판매 종료"),
    SOLD_OUT("매진");

    private final String description;

    TicketStatus(String description) {
        this.description = description;
    }
}
