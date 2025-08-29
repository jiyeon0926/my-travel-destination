package jiyeon.travel.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400 BAD_REQUEST
    PASSWORD_NOT_MATCH(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    PASSWORD_SAME_AS_OLD(BAD_REQUEST, "동일한 비밀번호로 변경할 수 없습니다."),

    // 404 NOT_FOUND
    USER_NOT_FOUND(NOT_FOUND, "사용자를 찾을 수 없습니다."),
    PARTNER_NOT_FOUND(NOT_FOUND, "업체를 찾을 수 없습니다."),

    // 409 CONFLICT
    EMAIL_ALREADY_EXISTS(CONFLICT, "이미 존재하는 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(CONFLICT, "이미 존재하는 별명입니다."),
    BUSINESS_NUMBER_ALREADY_EXISTS(CONFLICT, "동일한 사업장번호가 존재합니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
