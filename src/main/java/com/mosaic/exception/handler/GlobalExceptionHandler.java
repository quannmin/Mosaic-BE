package com.mosaic.exception.handler;

import com.mosaic.domain.response.ApiResponse;
import com.mosaic.exception.custom.*;
import com.mosaic.exception.model.ApiError;
import com.mosaic.exception.model.FieldError;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        HttpStatus status = determineHttpStatus(ex);

        ApiError apiError = ApiError.builder()
                .status(status.value())
                .message(ex.getMessage())
                .errorCode(ex.getErrorCode())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(apiError);
    }

    private HttpStatus determineHttpStatus(BusinessException ex) {
        if (ex instanceof ResourceNotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else if (ex instanceof DuplicateResourceException) {
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.BAD_REQUEST;
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

    @ExceptionHandler(UserNotActivatedException.class)
    public ResponseEntity<ApiError> handleUserNotActivatedException(
            UserNotActivatedException ex, HttpServletRequest request) {

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message(ex.getMessage())
                .errorCode(ex.getErrorCode())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> com.mosaic.exception.model.FieldError.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .rejectedValue(error.getRejectedValue() != null ? error.getRejectedValue().toString() : null)
                        .build())
                .toList();

        String errorMessage = "Validation failed for request";

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .errorCode("VALIDATION_ERROR")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(apiError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("Access denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message("Access denied: " + ex.getMessage())
                        .code(HttpStatus.FORBIDDEN.value())
                        .build());
    }
}
