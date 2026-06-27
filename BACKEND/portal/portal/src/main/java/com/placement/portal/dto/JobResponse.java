package com.placement.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Full job shape sent back to the frontend.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String requiredSkills;
    private String location;
    private String jobType;
    private Double salary;
    private Long companyId;
    private String companyName;
    private String status;
    private LocalDateTime postedDate;
}