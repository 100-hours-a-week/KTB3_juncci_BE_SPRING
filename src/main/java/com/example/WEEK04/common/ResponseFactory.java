package com.example.WEEK04.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseFactory {

    public <T> ResponseEntity<ApiResponseDto<T>> ok(T data) {
        return ResponseEntity.ok(new ApiResponseDto<>("ok", "요청이 성공했습니다.", data));
    }

    public <T> ResponseEntity<ApiResponseDto<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>("created", "리소스가 생성되었습니다.", data));
    }

    public ResponseEntity<ApiResponseDto<Void>> noContent() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponseDto<>("no_content", "성공했지만 반환할 데이터가 없습니다.", null));
    }


}
