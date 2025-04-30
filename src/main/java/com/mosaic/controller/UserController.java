package com.mosaic.controller;

import com.mosaic.domain.request.UserCreateRequest;
import com.mosaic.domain.request.UserUpdateRequest;
import com.mosaic.domain.response.ApiResponse;
import com.mosaic.domain.response.UserResponse;
import com.mosaic.service.spec.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        UserResponse userResponse = userService.createUser(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<UserResponse>builder()
                        .data(userResponse)
                        .code(HttpStatus.CREATED.value())
                        .message("User created")
                        .success(true)
                        .build()
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable Long id,
                                                   @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        UserResponse userResponse = userService.updateUser(id, userUpdateRequest);
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .data(userResponse)
                .code(HttpStatus.OK.value())
                .message("User updated")
                .success(true)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse userResponse = userService.findUserResponseById(id);
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .data(userResponse)
                .code(HttpStatus.OK.value())
                .message("fetch user by id successfully!")
                .success(true)
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.<List<UserResponse>>builder()
                .data(users)
                .code(HttpStatus.OK.value())
                .message("fetch all users successfully!")
                .success(true)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("User deleted successfully")
                .code(HttpStatus.OK.value())
                .data(null)
                .build());
    }
}
