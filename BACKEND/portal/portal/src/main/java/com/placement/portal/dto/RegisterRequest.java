package com.placement.portal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * This is the shape of the JSON the frontend sends when a student registers.
 * Example JSON body from React:
 * {
 *   "fullName": "Abi Kumar",
 *   "email": "abi@example.com",
 *   "password": "secret123",
 *   "phoneNumber": "9876543210",
 *   "department": "CSE"
 * }
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String phoneNumber;

    private String department;
}