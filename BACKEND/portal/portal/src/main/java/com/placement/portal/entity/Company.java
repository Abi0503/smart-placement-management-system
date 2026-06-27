package com.placement.portal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a row in the "companies" table.
 * This is a SEPARATE entity from Student -- companies and students have
 * different fields and log in through different endpoints, but both end up
 * using the SAME JwtUtil/JwtFilter machinery you built in Module 1.
 */
@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // BCrypt hashed, same as Student

    private String website;

    private String industry;

    @Column(length = 1000)
    private String description;

    private String contactPersonName;

    private String contactPhoneNumber;

    private String role = "COMPANY";

    private LocalDateTime createdAt = LocalDateTime.now();
}