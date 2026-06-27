package com.placement.portal.controller;

import com.placement.portal.dto.CompanyProfileResponse;
import com.placement.portal.dto.CompanyProfileUpdateRequest;
import com.placement.portal.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * PROTECTED endpoints -- require a valid JWT (any role), same as /api/student/**.
 *   GET /api/company/profile
 *   PUT /api/company/profile
 */
@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/profile")
    public ResponseEntity<CompanyProfileResponse> getProfile(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(companyService.getProfile(email));
    }

    @PutMapping("/profile")
    public ResponseEntity<CompanyProfileResponse> updateProfile(
            Authentication authentication,
            @RequestBody CompanyProfileUpdateRequest request) {
        String email = authentication.getName();
        return ResponseEntity.ok(companyService.updateProfile(email, request));
    }
}