package com.placement.portal.controller;

import com.placement.portal.dto.ProfileUpdateRequest;
import com.placement.portal.dto.StudentProfileResponse;
import com.placement.portal.exception.ApiException;
import com.placement.portal.service.StudentService;
import com.placement.portal.storage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;

/**
 * All endpoints here are PROTECTED -- SecurityConfig requires a valid JWT
 * for anything that isn't /api/auth/**. The JwtFilter you built in Module 1
 * is what makes "Authentication authentication" below automatically contain
 * the logged-in student's email -- React must send the token in the
 * Authorization header for these to work.
 *
 *   GET  /api/student/profile
 *   PUT  /api/student/profile
 *   POST /api/student/resume
 *   GET  /api/student/resume
 */
@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final FileStorageService fileStorageService;

    @GetMapping("/profile")
    public ResponseEntity<StudentProfileResponse> getProfile(Authentication authentication) {
        String email = authentication.getName(); // set by JwtFilter after validating the token
        return ResponseEntity.ok(studentService.getProfile(email));
    }

    @PutMapping("/profile")
    public ResponseEntity<StudentProfileResponse> updateProfile(
            Authentication authentication,
            @RequestBody ProfileUpdateRequest request) {
        String email = authentication.getName();
        return ResponseEntity.ok(studentService.updateProfile(email, request));
    }

    @PostMapping("/resume")
    public ResponseEntity<StudentProfileResponse> uploadResume(
            Authentication authentication,
            @RequestParam("file") MultipartFile file) {
        String email = authentication.getName();
        return ResponseEntity.ok(studentService.uploadResume(email, file));
    }

    @GetMapping("/resume")
    public ResponseEntity<Resource> downloadResume(Authentication authentication) {
        String email = authentication.getName();
        StudentProfileResponse profile = studentService.getProfile(email);

        if (profile.getResumeFileName() == null) {
            throw new ApiException("No resume uploaded yet");
        }

        try {
            Path filePath = fileStorageService.getResumePath(profile.getResumeFileName());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new ApiException("Resume file not found on server");
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + profile.getResumeFileName() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            throw new ApiException("Could not load resume file");
        }
    }
}