package com.placement.portal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Shape of the JSON sent when a company logs in.
 * POST /api/company/auth/login
 */
@Data
public class CompanyLoginRequest {

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}