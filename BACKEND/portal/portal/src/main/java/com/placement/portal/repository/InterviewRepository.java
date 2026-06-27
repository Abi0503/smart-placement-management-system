package com.placement.portal.repository;

import com.placement.portal.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    // Used to show a student all their scheduled interviews
    List<Interview> findByStudentId(Long studentId);

    // Used to show a company all interviews it has scheduled
    List<Interview> findByCompanyId(Long companyId);

    // Used to prevent scheduling two interviews for the same application
    Optional<Interview> findByApplicationId(Long applicationId);
}