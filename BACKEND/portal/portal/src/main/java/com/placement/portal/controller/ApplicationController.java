package com.placement.portal.controller;

import com.placement.portal.dto.ApplicationResponse;
import com.placement.portal.dto.ApplicationStatusUpdateRequest;
import com.placement.portal.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * All endpoints here are PROTECTED -- require a valid JWT.
 *
 *   POST /api/applications/apply/{jobId}     -> STUDENT applies to a job
 *   GET  /api/applications/my-applications   -> STUDENT views their own applications
 *   GET  /api/applications/job/{jobId}        -> COMPANY views applicants for one of its jobs
 *   PUT  /api/applications/{id}/status        -> COMPANY updates an applicant's status
 */
@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/apply/{jobId}")
    public ResponseEntity<ApplicationResponse> applyToJob(
            Authentication authentication,
            @PathVariable Long jobId) {
        String studentEmail = authentication.getName();
        return ResponseEntity.ok(applicationService.applyToJob(studentEmail, jobId));
    }

    @GetMapping("/my-applications")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications(Authentication authentication) {
        String studentEmail = authentication.getName();
        return ResponseEntity.ok(applicationService.getMyApplications(studentEmail));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationResponse>> getApplicantsForJob(
            Authentication authentication,
            @PathVariable Long jobId) {
        String companyEmail = authentication.getName();
        return ResponseEntity.ok(applicationService.getApplicantsForJob(companyEmail, jobId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody ApplicationStatusUpdateRequest request) {
        String companyEmail = authentication.getName();
        return ResponseEntity.ok(applicationService.updateApplicationStatus(companyEmail, id, request.getStatus()));
    }
}