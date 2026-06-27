package com.placement.portal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a row in the "notifications" table.
 * Generic enough to notify EITHER a student or a company -- we identify
 * the recipient by email, since that's unique across both account types
 * within their own table (and we always know which table to look in
 * because of recipientType).
 */
@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String recipientEmail;

    @Column(nullable = false)
    private String recipientType; // "STUDENT" or "COMPANY"

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String message;

    // e.g. "APPLICATION_STATUS", "INTERVIEW_SCHEDULED", "INTERVIEW_RESULT", "NEW_APPLICATION"
    private String type;

    private boolean isRead = false;

    private LocalDateTime createdAt = LocalDateTime.now();
}