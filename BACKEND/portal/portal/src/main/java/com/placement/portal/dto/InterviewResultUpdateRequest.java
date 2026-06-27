package com.placement.portal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Shape of the JSON a COMPANY sends to update an interview's result/status.
 * Used for PUT /api/interviews/{id}/result
 *
 * Example: { "status": "COMPLETED", "result": "PASSED" }
 */
@Data
public class InterviewResultUpdateRequest {

    @NotBlank(message = "Status is required")
    private String status; // SCHEDULED, COMPLETED, CANCELLED

    @NotBlank(message = "Result is required")
    private String result; // PENDING, PASSED, FAILED
}