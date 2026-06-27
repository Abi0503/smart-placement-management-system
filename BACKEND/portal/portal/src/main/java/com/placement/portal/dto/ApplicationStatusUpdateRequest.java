package com.placement.portal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Shape of the JSON a COMPANY sends to update an applicant's status.
 * Used for PUT /api/applications/{id}/status
 *
 * Example: { "status": "SHORTLISTED" }
 */
@Data
public class ApplicationStatusUpdateRequest {

    @NotBlank(message = "Status is required")
    private String status; // APPLIED, SHORTLISTED, REJECTED, SELECTED
}