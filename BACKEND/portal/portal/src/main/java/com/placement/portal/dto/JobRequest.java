package com.placement.portal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Shape of the JSON sent when a company creates or updates a job.
 * Used for POST /api/jobs and PUT /api/jobs/{id}
 */
@Data
public class JobRequest {

    @NotBlank(message = "Job title is required")
    private String title;

    private String description;
    private String requiredSkills; // comma-separated, e.g. "Java,React,SQL"
    private String location;
    private String jobType; // Full-time, Internship, Part-time
    private Double salary;
}