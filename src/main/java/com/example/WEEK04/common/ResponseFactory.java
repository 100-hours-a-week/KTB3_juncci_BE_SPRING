package com.example.WEEK04.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * ✅ ResponseFactory
 * 응답 생성을 단일화하여 SRP/OCP를 강화하는 클래스
 */
@Component
public class ResponseFactory {

    public <T> ResponseEntity<Response<T>> ok(T data) {
        return ResponseEntity.ok(new Response<>("ok", "요청이 성공했습니다.", data));
    }

    public <T> ResponseEntity<Response<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Response<>("created", "리소스가 생성되었습니다.", data));
    }

    public ResponseEntity<Response<Void>> noContent() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new Response<>("no_content", "성공했지만 반환할 데이터가 없습니다.", null));
    }

    public <T> ResponseEntity<Response<T>> badRequest(String code, String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Response<>(code, message, null));
    }

    public record Response<T>(String code, String message, T data) {}
}
