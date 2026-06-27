package com.placement.portal.controller;

import com.placement.portal.dto.JobRequest;
import com.placement.portal.dto.JobResponse;
import com.placement.portal.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * All endpoints here are PROTECTED -- require a valid JWT.
 * These are meant to be called by a logged-in COMPANY, to manage
 * its own job postings.
 *
 *   POST   /api/jobs          -> create a new job
 *   GET    /api/jobs/my-jobs  -> get all jobs posted by the logged-in company
 *   PUT    /api/jobs/{id}     -> update a job (only if it belongs to this company)
 *   DELETE /api/jobs/{id}     -> delete a job (only if it belongs to this company)
 */
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<JobResponse> createJob(
            Authentication authentication,
            @Valid @RequestBody JobRequest request) {
        String companyEmail = authentication.getName();
        return ResponseEntity.ok(jobService.createJob(companyEmail, request));
    }

    @GetMapping("/my-jobs")
    public ResponseEntity<List<JobResponse>> getMyJobs(Authentication authentication) {
        String companyEmail = authentication.getName();
        return ResponseEntity.ok(jobService.getMyJobs(companyEmail));
    }

    // Module 5: any logged-in student (or company) can browse all open jobs
    @GetMapping("/open")
    public ResponseEntity<List<JobResponse>> getOpenJobs() {
        return ResponseEntity.ok(jobService.getAllOpenJobs());
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponse> updateJob(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody JobRequest request) {
        String companyEmail = authentication.getName();
        return ResponseEntity.ok(jobService.updateJob(companyEmail, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(
            Authentication authentication,
            @PathVariable Long id) {
        String companyEmail = authentication.getName();
        jobService.deleteJob(companyEmail, id);
        return ResponseEntity.noContent().build();
    }
}