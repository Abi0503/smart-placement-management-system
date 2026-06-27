package com.placement.portal.repository;

import com.placement.portal.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // Used to show a student all the jobs they've applied to
    List<Application> findByStudentId(Long studentId);

    // Used to show a company all applicants for a specific job
    List<Application> findByJobId(Long jobId);

    // Used to prevent a student from applying to the same job twice
    Optional<Application> findByStudentIdAndJobId(Long studentId, Long jobId);
}