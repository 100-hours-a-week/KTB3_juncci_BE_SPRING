package com.example.WEEK04.exception;

import com.example.WEEK04.model.dto.response.SignupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<SignupResponse> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.status(errorCode.getStatus())
                .body(new SignupResponse(
                        mapMessage(errorCode),
                        null,
                        new ErrorDetail(errorCode.getCode(), errorCode.getMessage())
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SignupResponse> handleServerError(Exception e) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER;

        return ResponseEntity.status(errorCode.getStatus())
                .body(new SignupResponse(
                        mapMessage(errorCode),
                        null,
                        new ErrorDetail(errorCode.getCode(), errorCode.getMessage())
                ));
    }

    private String mapMessage(ErrorCode ec) {
        return switch (ec) {
            case EMAIL_DUPLICATE, LOGIN_FAIL -> "conflict";
            case EMAIL_INVALID, PASSWORD_INVALID, NICKNAME_INVALID, PROFILE_IMAGE_INVALID, POST_TITLE_LEN -> "invalid_request";
            case FIELD_MISSING, POST_FIELD_MISSING -> "invalid_request";
            case AUTH_TOKEN_INVALID -> "unauthorized";
            case AUTH_FORBIDDEN -> "forbidden";
            case POST_NOT_FOUND -> "not_found";
            case INTERNAL_SERVER -> "internal_server_error";
            default -> "internal_server_error"; // 새 에러코드가 추가되더라도 기본값 처리
        };
    }

    static class ErrorDetail {
        private final String code;
        private final String detail;

        public ErrorDetail(String code, String detail) {
            this.code = code;
            this.detail = detail;
        }

        public String getCode() { return code; }
        public String getDetail() { return detail; }
    }
}
