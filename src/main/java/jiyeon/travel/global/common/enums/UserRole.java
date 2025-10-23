package jiyeon.travel.global.common.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public enum UserRole {

    USER("사용자"),
    PARTNER("업체"),
    ADMIN("관리자");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + name()));
    }
}
