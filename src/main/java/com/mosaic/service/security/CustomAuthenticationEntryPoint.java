package com.mosaic.service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mosaic.domain.response.ApiResponse;
import com.mosaic.exception.model.ApiError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiError errorResponse = ApiError.builder()
                .errorCode("AUTHENTICATION_FAILED")
                .status(401)
                .message(authException.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        if (authException instanceof BadCredentialsException) {
            errorResponse.setMessage("Tên đăng nhập hoặc mật khẩu không chính xác");
        } else if (authException instanceof LockedException) {
            errorResponse.setMessage("Tài khoản đã bị khóa");
        } else if (authException instanceof DisabledException) {
            errorResponse.setMessage("Tài khoản chưa được kích hoạt");
        }
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
