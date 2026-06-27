package com.placement.portal.controller;

import com.placement.portal.dto.InterviewRequest;
import com.placement.portal.dto.InterviewResponse;
import com.placement.portal.dto.InterviewResultUpdateRequest;
import com.placement.portal.service.InterviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * All endpoints here are PROTECTED -- require a valid JWT.
 *
 *   POST /api/interviews/schedule/{applicationId}  -> COMPANY schedules an interview
 *   GET  /api/interviews/my-interviews              -> STUDENT views their interviews
 *   GET  /api/interviews/company-interviews         -> COMPANY views all interviews it scheduled
 *   PUT  /api/interviews/{id}/result                 -> COMPANY updates status/result
 */
@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping("/schedule/{applicationId}")
    public ResponseEntity<InterviewResponse> scheduleInterview(
            Authentication authentication,
            @PathVariable Long applicationId,
            @Valid @RequestBody InterviewRequest request) {
        String companyEmail = authentication.getName();
        return ResponseEntity.ok(interviewService.scheduleInterview(companyEmail, applicationId, request));
    }

    @GetMapping("/my-interviews")
    public ResponseEntity<List<InterviewResponse>> getMyInterviews(Authentication authentication) {
        String studentEmail = authentication.getName();
        return ResponseEntity.ok(interviewService.getMyInterviews(studentEmail));
    }

    @GetMapping("/company-interviews")
    public ResponseEntity<List<InterviewResponse>> getCompanyInterviews(Authentication authentication) {
        String companyEmail = authentication.getName();
        return ResponseEntity.ok(interviewService.getCompanyInterviews(companyEmail));
    }

    @PutMapping("/{id}/result")
    public ResponseEntity<InterviewResponse> updateResult(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody InterviewResultUpdateRequest request) {
        String companyEmail = authentication.getName();
        return ResponseEntity.ok(interviewService.updateResult(companyEmail, id, request.getStatus(), request.getResult()));
    }
}