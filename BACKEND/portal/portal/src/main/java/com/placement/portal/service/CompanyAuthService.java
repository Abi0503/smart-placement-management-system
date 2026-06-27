package com.placement.portal.service;

import com.placement.portal.dto.AuthResponse;
import com.placement.portal.dto.CompanyLoginRequest;
import com.placement.portal.dto.CompanyRegisterRequest;
import com.placement.portal.entity.Company;
import com.placement.portal.exception.ApiException;
import com.placement.portal.repository.CompanyRepository;
import com.placement.portal.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Mirrors AuthService (Module 1) but for companies instead of students.
 * Reuses the SAME JwtUtil and AuthenticationManager -- companies and students
 * are authenticated through the exact same JWT mechanism, just different tables.
 */
@Service
@RequiredArgsConstructor
public class CompanyAuthService {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResponse register(CompanyRegisterRequest request) {
        if (companyRepository.existsByEmail(request.getEmail())) {
            throw new ApiException("Email already registered. Please login instead.");
        }

        Company company = new Company();
        company.setCompanyName(request.getCompanyName());
        company.setEmail(request.getEmail());
        company.setPassword(passwordEncoder.encode(request.getPassword()));
        company.setWebsite(request.getWebsite());
        company.setIndustry(request.getIndustry());
        company.setDescription(request.getDescription());
        company.setContactPersonName(request.getContactPersonName());
        company.setContactPhoneNumber(request.getContactPhoneNumber());
        company.setRole("COMPANY");

        Company saved = companyRepository.save(company);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(saved.getEmail())
                .password(saved.getPassword())
                .authorities("ROLE_COMPANY")
                .build();

        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token, saved.getId(), saved.getCompanyName(), saved.getEmail(), saved.getRole());
    }

    public AuthResponse login(CompanyLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Company company = companyRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("Company not found"));

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(company.getEmail())
                .password(company.getPassword())
                .authorities("ROLE_" + company.getRole())
                .build();

        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token, company.getId(), company.getCompanyName(), company.getEmail(), company.getRole());
    }
}