package com.placement.portal.controller;

import com.placement.portal.dto.AuthResponse;
import com.placement.portal.dto.CompanyLoginRequest;
import com.placement.portal.dto.CompanyRegisterRequest;
import com.placement.portal.service.CompanyAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Public endpoints (no token required) for company register/login:
 *   POST http://localhost:8080/api/company/auth/register
 *   POST http://localhost:8080/api/company/auth/login
 */
@RestController
@RequestMapping("/api/company/auth")
@RequiredArgsConstructor
public class CompanyAuthController {

    private final CompanyAuthService companyAuthService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody CompanyRegisterRequest request) {
        return ResponseEntity.ok(companyAuthService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody CompanyLoginRequest request) {
        return ResponseEntity.ok(companyAuthService.login(request));
    }
}