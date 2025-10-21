package com.example.WEEK04.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공통 에러 응답")
public class ErrorResponse {

    @Schema(description = "에러 코드", example = "USR-EMAIL-INVALID")
    private final String code;

    @Schema(description = "에러 메시지", example = "이메일 형식이 올바르지 않습니다.")
    private final String message;

    @Schema(description = "상세 정보", example = "MethodArgumentNotValidException")
    private final String detail;

    public ErrorResponse(String code, String message, String detail) {
        this.code = code;
        this.message = message;
        this.detail = detail;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public String getDetail() { return detail; }
}
