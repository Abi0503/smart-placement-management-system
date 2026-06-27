package com.placement.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Full interview shape sent to the frontend.
 * Used both for the student's "My Interviews" view and the company's view.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewResponse {
    private Long id;
    private Long applicationId;
    private Long studentId;
    private String studentName;
    private String studentEmail;
    private Long jobId;
    private String jobTitle;
    private Long companyId;
    private String companyName;
    private LocalDateTime scheduledAt;
    private String mode;
    private String locationOrLink;
    private String notes;
    private String status;
    private String result;
}