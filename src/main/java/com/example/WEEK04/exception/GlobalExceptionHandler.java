package com.example.WEEK04.exception;

import com.example.WEEK04.model.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ApiResponse(
            responseCode = "500",
            description = "서버 내부 오류",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    public ResponseEntity<ErrorResponse> handleServerError(Exception e) {
        ErrorResponse error = new ErrorResponse(
                "SYS-UNEXPECTED",
                "예상치 못한 오류가 발생했습니다.",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
