package com.example.userapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @Size(min = 2, max = 100, message = "Username should be at least 2 characters long, and less than 100")
    private String username;

    @Email(message = "Not a valid email")
    private String email;

    @Size(min = 6, message = "Password should be at least 6 characters long")
    private String password;

    private boolean active;

}
