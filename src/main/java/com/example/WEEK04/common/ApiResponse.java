package com.example.WEEK04.common;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공통 API 응답 포맷")
public class ApiResponse<T> {
    @Schema(description = "응답 코드", example = "ok")
    private String code;

    @Schema(description = "메시지", example = "요청이 성공했습니다.")
    private String message;

    @Schema(description = "데이터 본문")
    private T data;

    public ApiResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}
