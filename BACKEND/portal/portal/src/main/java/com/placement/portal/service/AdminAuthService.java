package com.placement.portal.service;

import com.placement.portal.dto.AdminLoginRequest;
import com.placement.portal.dto.AuthResponse;
import com.placement.portal.entity.Admin;
import com.placement.portal.exception.ApiException;
import com.placement.portal.repository.AdminRepository;
import com.placement.portal.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Deliberately has NO register() method -- unlike students/companies,
 * admin accounts are not self-service signups.
 */
@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminRepository adminRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResponse login(AdminLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("Admin not found"));

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(admin.getEmail())
                .password(admin.getPassword())
                .authorities("ROLE_" + admin.getRole())
                .build();

        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token, admin.getId(), admin.getFullName(), admin.getEmail(), admin.getRole());
    }
}