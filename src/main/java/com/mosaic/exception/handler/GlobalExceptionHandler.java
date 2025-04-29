package com.mosaic.exception.handler;

import com.mosaic.domain.response.ApiResponse;
import com.mosaic.exception.custom.*;
import com.mosaic.exception.model.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
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

}
