package com.placement.portal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Shape of the JSON sent when a student logs in.
 * {
 *   "email": "abi@example.com",
 *   "password": "secret123"
 * }
 */
@Data
public class LoginRequest {

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}