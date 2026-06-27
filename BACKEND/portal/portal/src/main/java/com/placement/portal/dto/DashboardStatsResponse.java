package com.placement.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Summary counts shown on the Admin Dashboard.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {
    private long totalStudents;
    private long totalCompanies;
    private long totalJobs;
    private long openJobs;
    private long totalApplications;
    private long totalInterviews;
    private long selectedCount;
}