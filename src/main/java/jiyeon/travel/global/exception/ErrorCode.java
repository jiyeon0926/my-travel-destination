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
    BASE_PRICE_EMPTY(BAD_REQUEST, "기본 가격이 없을 경우, 옵션은 반드시 존재해야 합니다."),
    BASE_PRICE_PRESENT(BAD_REQUEST, "기본 가격이 있을 경우, 옵션은 저장할 수 없습니다."),
    IMAGE_MAX_COUNT_EXCEEDED(BAD_REQUEST, "업로드 가능한 이미지 수를 초과했습니다."),
    IMAGE_ONLY_ALLOWED(BAD_REQUEST, "이미지 파일만 업로드할 수 있습니다."),
    FILE_UPLOAD_SIZE_EXCEEDED(BAD_REQUEST, "파일 업로드 크기는 최대 10MB까지 가능합니다."),
    TICKET_OPTION_PRESENT(BAD_REQUEST, "옵션이 존재할 경우, 기본 가격을 설정할 수 없습니다."),
    CANNOT_DELETE_TICKET_MAIN_IMAGE(BAD_REQUEST, "티켓 대표 이미지는 삭제할 수 없습니다."),
    NULL_TIME_SCHEDULE_DUPLICATE(BAD_REQUEST, "하루 기준으로 시간 없는 일정은 중복 등록할 수 없습니다."),
    SCHEDULE_OUT_OF_SALE_RANGE(BAD_REQUEST, "일정은 판매 시작일부터 종료일까지의 범위 내에서만 등록 가능합니다."),
    TICKET_READY_ONLY(BAD_REQUEST, "티켓이 판매 전일 때만 추가/수정/삭제 할 수 있습니다."),
    TICKET_IMAGE_UPDATE_NOT_ALLOWED(BAD_REQUEST, "티켓이 판매 중지/판매 종료/매진일 경우, 이미지 추가/삭제/대표 이미지 변경이 불가합니다."),
    INVALID_SALE_START_DATE(BAD_REQUEST, "판매 시작일은 현재 시간 이후여야 합니다."),
    INVALID_SALE_END_DATE(BAD_REQUEST, "판매 종료일은 시작일 이후여야 합니다."),
    INVALID_STATUS_CHANGE(BAD_REQUEST, "다른 판매 상태로 변경할 수 없습니다."),
    INVALID_TICKET_INACTIVE_CHANGE(BAD_REQUEST, "판매 중 상태일 때만 판매 중지 상태로 변경할 수 있습니다."),
    BASE_TICKET_QUANTITY_REQUIRED(BAD_REQUEST, "옵션이 없는 티켓은 기본 수량이 필요합니다."),
    BASE_TICKET_QUANTITY_NOT_ALLOWED(BAD_REQUEST, "옵션이 있는 티켓은 기본 수량이 필요 없습니다."),
    NO_OPTION_TICKET(BAD_REQUEST, "옵션이 없는 티켓입니다."),
    HAS_OPTION_TICKET(BAD_REQUEST, "옵션이 포함된 티켓입니다."),
    RESERVATION_ONLY_WHEN_ON_SALE(BAD_REQUEST, "판매 중일 때만 예약할 수 있습니다."),
    DUPLICATE_OPTION(BAD_REQUEST, "동일한 옵션이 존재합니다."),

    // 404 NOT_FOUND
    USER_NOT_FOUND(NOT_FOUND, "사용자를 찾을 수 없습니다."),
    PARTNER_NOT_FOUND(NOT_FOUND, "업체를 찾을 수 없습니다."),
    TICKET_NOT_FOUND(NOT_FOUND, "티켓을 찾을 수 없습니다."),
    TICKET_IMAGE_NOT_FOUND(NOT_FOUND, "티켓 이미지를 찾을 수 없습니다."),
    TICKET_SCHEDULE_NOT_FOUND(NOT_FOUND, "티켓 일정을 찾을 수 없습니다."),
    TICKET_OPTION_NOT_FOUND(NOT_FOUND, "티켓 옵션을 찾을 수 없습니다."),

    // 409 CONFLICT
    EMAIL_ALREADY_EXISTS(CONFLICT, "이미 존재하는 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(CONFLICT, "이미 존재하는 별명입니다."),
    BUSINESS_NUMBER_ALREADY_EXISTS(CONFLICT, "동일한 사업장번호가 존재합니다."),
    ALREADY_TICKET_MAIN_IMAGE(CONFLICT, "이미 대표 이미지입니다."),
    TICKET_SCHEDULE_ALREADY_EXISTS(CONFLICT, "동일한 일정이 존재합니다."),

    // 500
    FILE_UPLOAD_FAILED(INTERNAL_SERVER_ERROR, "파일 업로드를 실패하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
