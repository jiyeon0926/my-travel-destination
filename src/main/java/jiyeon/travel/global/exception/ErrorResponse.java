package jiyeon.travel.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final String code;
    private final int status;
    private final String message;
    private final LocalDateTime timestamp;

    public static ResponseEntity<ErrorResponse> of(ErrorCode errorCode) {
        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.name(),
                errorCode.getHttpStatus().value(),
                errorCode.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(errorResponse);
    }
}
