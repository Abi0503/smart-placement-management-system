package com.placement.portal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Shape of the JSON a COMPANY sends to schedule an interview.
 * Used for POST /api/interviews/schedule/{applicationId}
 */
@Data
public class InterviewRequest {

    @NotNull(message = "Scheduled date/time is required")
    private LocalDateTime scheduledAt;

    @NotBlank(message = "Mode is required (Online or Offline)")
    private String mode;

    private String locationOrLink;
    private String notes;
}