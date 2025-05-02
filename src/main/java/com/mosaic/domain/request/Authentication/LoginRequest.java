package com.mosaic.domain.request.Authentication;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    @NotNull
    @Size(min = 1, max = 50)
    String input;
    @NotNull
    @Size(min = 4, max = 100, message = "Password must be at least 4 characters")
    String password;

    boolean rememberMe;
}
