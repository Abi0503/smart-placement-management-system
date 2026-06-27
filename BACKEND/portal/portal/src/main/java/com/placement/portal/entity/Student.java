package com.placement.portal.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents a row in the "students" table.
 * Every field below becomes a column in the database automatically,
 * thanks to Hibernate (because of spring.jpa.hibernate.ddl-auto=update).
 */
@Entity
@Table(name = "students")
@Data               // Lombok: auto-generates getters, setters, toString, equals, hashCode
@NoArgsConstructor   // Lombok: empty constructor (required by JPA)
@AllArgsConstructor  // Lombok: constructor with all fields
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment primary key
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true) // no two students can register with the same email
    private String email;

    @Column(nullable = false)
    private String password; // this will store the HASHED password, never plain text

    private String phoneNumber;

    private String department;

    private String role = "STUDENT"; // useful later when Company/Admin modules are added

    private LocalDateTime createdAt = LocalDateTime.now();

    // ===================== Module 2: Academic Details =====================
    private String collegeName;
    private String branch;
    private Double tenthPercentage;
    private Double twelfthPercentage;
    private Double cgpa;
    private Integer passingYear;

    // ===================== Module 2: Skills =====================
    // Stored as comma-separated text for simplicity, e.g. "Java,React,SQL"
    @Column(length = 1000)
    private String skills;

    // ===================== Module 2: Resume =====================
    // Stores just the filename on disk (e.g. "3_resume.pdf"), not the file itself
    private String resumeFileName;
}