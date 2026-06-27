package com.placement.portal.controller;

import com.placement.portal.dto.CompanyProfileResponse;
import com.placement.portal.dto.DashboardStatsResponse;
import com.placement.portal.dto.JobResponse;
import com.placement.portal.dto.StudentProfileResponse;
import com.placement.portal.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * All endpoints here require ROLE_ADMIN specifically -- NOT just "any
 * logged-in user". This is different from the "ownership" checks used
 * elsewhere: here it's about WHO YOU ARE (your role), not WHICH RECORD
 * you're touching.
 *
 *   GET    /api/admin/dashboard-stats
 *   GET    /api/admin/students
 *   GET    /api/admin/companies
 *   GET    /api/admin/jobs
 *   DELETE /api/admin/students/{id}
 *   DELETE /api/admin/companies/{id}
 *   DELETE /api/admin/jobs/{id}
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard-stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentProfileResponse>> getAllStudents() {
        return ResponseEntity.ok(adminService.getAllStudents());
    }

    @GetMapping("/companies")
    public ResponseEntity<List<CompanyProfileResponse>> getAllCompanies() {
        return ResponseEntity.ok(adminService.getAllCompanies());
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        return ResponseEntity.ok(adminService.getAllJobs());
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        adminService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        adminService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        adminService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}