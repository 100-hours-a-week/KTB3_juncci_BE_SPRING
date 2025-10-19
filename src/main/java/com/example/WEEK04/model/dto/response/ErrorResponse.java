package com.example.WEEK04.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공통 에러 응답")
public class ErrorResponse {

    @Schema(description = "에러 코드", example = "SYS-UNEXPECTED")
    private final String code;

    @Schema(description = "에러 메시지", example = "예상치 못한 오류가 발생했습니다.")
    private final String message;

    @Schema(description = "추가 상세정보", example = "NullPointerException at line 32")
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
