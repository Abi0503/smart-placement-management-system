package com.placement.portal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a row in the "jobs" table.
 * Each job is linked to the Company that posted it via companyId.
 * We don't use a JPA @ManyToOne relationship here on purpose -- storing
 * just the plain companyId keeps things simple and avoids lazy-loading
 * complexity for a student project. We look up the Company manually
 * in JobService when we need company details.
 */
@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    // Comma-separated, same pattern as Student.skills, e.g. "Java,Spring Boot,SQL"
    @Column(length = 1000)
    private String requiredSkills;

    private String location;

    // e.g. "Full-time", "Internship", "Part-time"
    private String jobType;

    private Double salary;

    // Which company posted this job -- links back to Company.id
    @Column(nullable = false)
    private Long companyId;

    // Denormalized for convenience, so we don't always need a join/lookup just to show a name
    private String companyName;

    private String status = "OPEN"; // OPEN or CLOSED

    private LocalDateTime postedDate = LocalDateTime.now();
}
