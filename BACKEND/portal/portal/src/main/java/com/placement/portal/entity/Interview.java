package com.placement.portal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a row in the "interviews" table.
 * Linked to a specific Application (a student's application to a job).
 */
@Entity
@Table(name = "interviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long applicationId;

    private Long studentId;
    private String studentName;
    private String studentEmail;
    private Long jobId;
    private String jobTitle;
    private Long companyId;
    private String companyName;

    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    private String mode; // "Online" or "Offline"
    private String locationOrLink;
    private String notes;

    private String status = "SCHEDULED"; // SCHEDULED, COMPLETED, CANCELLED
    private String result = "PENDING"; // PENDING, PASSED, FAILED

    private LocalDateTime createdAt = LocalDateTime.now();
}