package jiyeon.travel.global.common.enums;

public enum UserRole {

    USER("사용자"),
    PARTNER("업체"),
    ADMIN("관리자");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }
}
