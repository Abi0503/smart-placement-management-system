package com.placement.portal.repository;

import com.placement.portal.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    // Used so a company only ever sees/manages its OWN job postings
    List<Job> findByCompanyId(Long companyId);
}
