package com.placement.portal.dto;

import lombok.Data;

/**
 * Shape of the JSON sent when a student updates their profile.
 * Used for PUT /api/student/profile
 */
@Data
public class ProfileUpdateRequest {
    private String fullName;
    private String phoneNumber;
    private String department;
    private String collegeName;
    private String branch;
    private Double tenthPercentage;
    private Double twelfthPercentage;
    private Double cgpa;
    private Integer passingYear;
    private String skills; // comma-separated, e.g. "Java,React,SQL"
}