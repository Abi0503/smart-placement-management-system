package com.placement.portal.service;

import com.placement.portal.dto.AuthResponse;
import com.placement.portal.dto.LoginRequest;
import com.placement.portal.dto.RegisterRequest;
import com.placement.portal.entity.Student;
import com.placement.portal.exception.ApiException;
import com.placement.portal.repository.StudentRepository;
import com.placement.portal.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // Lombok creates a constructor for all 'final' fields below -> Spring injects them automatically
public class AuthService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        // Step 1: Make sure this email isn't already used
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new ApiException("Email already registered. Please login instead.");
        }

        // Step 2: Create the Student object, HASH the password before saving
        Student student = new Student();
        student.setFullName(request.getFullName());
        student.setEmail(request.getEmail());
        student.setPassword(passwordEncoder.encode(request.getPassword())); // never save plain text
        student.setPhoneNumber(request.getPhoneNumber());
        student.setDepartment(request.getDepartment());
        student.setRole("STUDENT");

        Student saved = studentRepository.save(student);

        // Step 3: Generate a JWT so the student is immediately logged in after registering
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(saved.getEmail())
                .password(saved.getPassword())
                .authorities("ROLE_STUDENT")
                .build();

        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token, saved.getId(), saved.getFullName(), saved.getEmail(), saved.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        // Step 1: Let Spring Security check the email + password.
        // If the password doesn't match, it automatically throws BadCredentialsException,
        // which our GlobalExceptionHandler catches and turns into a clean 401 response.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Step 2: Password was correct -- now fetch the student record
        Student student = studentRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("Student not found"));

        // Step 3: Generate a fresh token for this login session
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(student.getEmail())
                .password(student.getPassword())
                .authorities("ROLE_" + student.getRole())
                .build();

        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token, student.getId(), student.getFullName(), student.getEmail(), student.getRole());
    }
}