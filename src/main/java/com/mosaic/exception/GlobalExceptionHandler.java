package com.mosaic.exception;

import com.mosaic.domain.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ElementNotFoundException.class)
    ResponseEntity<ApiResponse<Object>> handleElementNotFoundException(ElementNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(ApiResponse.builder()
                        .message(e.getMessage())
                        .code(HttpStatus.NOT_FOUND.value())
                        .success(false)
                        .build());
    }

    @ExceptionHandler(ElementExistedException.class)
    ResponseEntity<ApiResponse<Object>> handleElementExistedException(ElementExistedException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT.value())
                .body(ApiResponse.builder()
                        .message(e.getMessage())
                        .code(HttpStatus.CONFLICT.value())
                        .success(false)
                        .build());
    }

    @ExceptionHandler(DuplicateElementException.class)
    ResponseEntity<ApiResponse<Object>> handleDuplicateElementException(DuplicateElementException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT.value())
                .body(ApiResponse.builder()
                        .message(e.getMessage())
                        .code(HttpStatus.CONFLICT.value())
                        .success(false)
                        .build());
    }

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<ApiResponse<Object>> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(ApiResponse.builder()
                        .message(e.getMessage())
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .success(false)
                        .build());
    }

    @ExceptionHandler(InsufficientStockException.class)
    ResponseEntity<ApiResponse<Object>> handleInsufficientStockException(InsufficientStockException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(ApiResponse.builder()
                        .message(e.getMessage())
                        .code(HttpStatus.BAD_REQUEST.value())
                        .success(false)
                        .build());
    }
}
