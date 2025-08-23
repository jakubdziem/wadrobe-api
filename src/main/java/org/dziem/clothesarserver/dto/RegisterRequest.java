package org.dziem.clothesarserver.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotNull
    private String username;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$")
    private String password;
}

