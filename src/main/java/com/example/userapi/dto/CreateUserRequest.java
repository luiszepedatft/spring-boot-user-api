package com.example.userapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 2, max = 100, message = "Username should be at least 2 characters long, and less than 100")
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank(message = "Username is required")
    @Size(min = 6, message = "Password should be at least 6 characters long")
    private String password;
}

