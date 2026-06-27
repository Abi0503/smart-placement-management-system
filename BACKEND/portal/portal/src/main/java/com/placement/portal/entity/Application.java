package com.placement.portal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a row in the "applications" table.
 * Links a Student to a Job they applied for.
 */
@Entity
@Table(name = "applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private Long jobId;

    private String studentName;
    private String studentEmail;
    private String jobTitle;
    private String companyName;
    private Long companyId;

    private String status = "APPLIED";

    private LocalDateTime appliedDate = LocalDateTime.now();
}