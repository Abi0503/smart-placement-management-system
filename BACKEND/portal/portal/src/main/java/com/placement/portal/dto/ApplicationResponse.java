package com.placement.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Full application shape sent to the frontend.
 * Used both for "my applications" (student view) and "applicants" (company view).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {
    private Long id;
    private Long studentId;
    private Long jobId;
    private String studentName;
    private String studentEmail;
    private String jobTitle;
    private String companyName;
    private Long companyId;
    private String status;
    private LocalDateTime appliedDate;
}