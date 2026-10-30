package com.rr.jobtracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Email is Required")
    @Email(message = "Please enter a valid email")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
