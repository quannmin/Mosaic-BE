package com.mosaic.controller;

import com.mosaic.domain.request.Authentication.*;
import com.mosaic.domain.response.ApiResponse;
import com.mosaic.domain.response.TokenRefreshResponse;
import com.mosaic.domain.response.UserResponse;
import com.mosaic.entity.RefreshToken;
import com.mosaic.entity.User;
import com.mosaic.exception.custom.ResourceNotFoundException;
import com.mosaic.exception.custom.TokenRefreshException;
import com.mosaic.service.security.SecurityUtils;
import com.mosaic.service.spec.AuthService;
import com.mosaic.service.spec.RefreshTokenService;
import com.mosaic.service.spec.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthenticateController {

    @Value("${app.security.authentication.jwt.refresh-token.token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;
    @Value("${app.security.authentication.jwt.refresh-token.token-validity-in-seconds-for-remember-me}")
    private long refreshTokenValidityInSecondsForRememberMe;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getInput(),
                loginRequest.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = authService.generateToken(authentication);

        long refreshTokenDuration = loginRequest.isRememberMe()
                ? refreshTokenValidityInSecondsForRememberMe
                : refreshTokenValidityInSeconds;

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(
                SecurityUtils.getUserIdFromAuthentication(),
                refreshTokenDuration
        );

        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken.getToken());

        if(loginRequest.isRememberMe()) {
            refreshTokenCookie.setMaxAge((int) refreshTokenDuration);
        }

        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");

        response.addCookie(refreshTokenCookie);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(ApiResponse.<String>builder()
                        .data(accessToken)
                        .message("Login successfully!")
                        .code(HttpStatus.OK.value())
                        .success(true)
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = null;
        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("refresh_token")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if(refreshToken == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.<String>builder()
                            .success(false)
                            .message("Refresh token not found in cookie!")
                            .code(HttpStatus.UNAUTHORIZED.value())
                            .build());
        }

        RefreshToken refreshTokenEntity = refreshTokenService.findByToken(refreshToken);
        boolean isValid = refreshTokenService.verifyExpiration(refreshTokenEntity);
        if(!isValid) {
            Cookie refreshTokenCookie = new Cookie("refresh_token", null);
            refreshTokenCookie.setMaxAge(0);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true);
            refreshTokenCookie.setPath("/");
            response.addCookie(refreshTokenCookie);

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.<String>builder()
                            .success(false)
                            .message("Refresh token has expired or revoked!")
                            .code(HttpStatus.UNAUTHORIZED.value())
                            .build());
        }
        refreshTokenEntity = refreshTokenService.updateExpiryDate(refreshTokenEntity, refreshTokenValidityInSeconds);
        String newAccessToken = authService.generateToken(refreshTokenEntity.getUser());

        long secondsUntilExpiry = refreshTokenEntity.getExpiryDate().getEpochSecond() - Instant.now().getEpochSecond();

        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshTokenEntity.getToken());
        refreshTokenCookie.setMaxAge((int) secondsUntilExpiry);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");

        response.addCookie(refreshTokenCookie);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .data(newAccessToken)
                .code(HttpStatus.OK.value())
                .success(true)
                .message("Fetch refresh token successfully!")
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register (@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.<UserResponse>builder()
                        .data(authService.register(registerRequest))
                        .message("Register user successfully!")
                        .code(HttpStatus.CREATED.value())
                        .success(true)
                        .build());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Boolean>> forgotPassword(@RequestBody PasswordRequests.ForgotPasswordRequest request) {
        boolean isValid = authService.forgotPassword(request);
        if (isValid) {
            return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                    .success(true)
                    .message("Send otp forgot password successfully!")
                    .code(HttpStatus.OK.value())
                    .data(true)
                    .build());
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.<Boolean>builder()
                    .success(false)
                    .message("Send otp forgot password failed!")
                    .code(HttpStatus.BAD_REQUEST.value())
                    .data(false)
                    .build());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Boolean>> verifyOtp(@RequestBody VerificationRequests.VerifyOtpRequest verifyOtpRequest) {
        boolean isValid = authService.verifyOtp(verifyOtpRequest);
        if (isValid) {
            return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                    .success(true)
                    .message("Mã OTP hợp lệ")
                    .code(HttpStatus.OK.value())
                    .data(true)
                    .build());
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.<Boolean>builder()
                    .success(false)
                    .message("Mã OTP không hợp lệ hoặc đã hết hạn")
                    .code(HttpStatus.BAD_REQUEST.value())
                    .data(false)
                    .build());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody PasswordRequests.ResetPasswordRequest request) {
        boolean success = authService.resetPassword(request);
        return getApiResponseResponseEntity(success);
    }


    @PostMapping("/users/{id}/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody PasswordRequests.ChangePasswordRequest request)
    {
        boolean success = authService.changePassword(id, request);
        return getApiResponseResponseEntity(success);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response, HttpServletRequest request) {
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("refresh_token")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);

        if(refreshToken != null) {
            refreshTokenService.deleteByToken(refreshToken);
        }

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Logged out successfully!")
                .code(HttpStatus.OK.value())
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> isAuthenticated(HttpServletRequest request) {
        String username = request.getRemoteUser();

        if (username != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("authenticated", true);
            response.put("username", username);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                response.put("authorities", auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));
            }

            return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .code(HttpStatus.OK.value())
                            .message("Authenticated")
                            .data(response)
                    .build());
        } else {
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .code(HttpStatus.OK.value())
                    .message("Unauthenticated")
                    .data(Map.of("authenticated", false))
                    .build());
        }
    }

    private ResponseEntity<ApiResponse<Void>> getApiResponseResponseEntity(boolean success) {
        if (success) {
            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .success(true)
                    .message("Mật khẩu đã được đặt lại thành công")
                    .code(HttpStatus.OK.value())
                    .build());
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
                    .success(false)
                    .message("Không thể đặt lại mật khẩu")
                    .code(HttpStatus.BAD_REQUEST.value())
                    .build());
        }
    }
}
