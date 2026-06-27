package com.placement.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Full profile shape sent BACK to the frontend.
 * Used by GET /api/student/profile and after PUT /api/student/profile.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfileResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String department;
    private String role;

    private String collegeName;
    private String branch;
    private Double tenthPercentage;
    private Double twelfthPercentage;
    private Double cgpa;
    private Integer passingYear;

    private String skills;
    private String resumeFileName;
}