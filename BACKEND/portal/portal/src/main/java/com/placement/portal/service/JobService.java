package com.placement.portal.service;

import com.placement.portal.dto.JobRequest;
import com.placement.portal.dto.JobResponse;
import com.placement.portal.entity.Company;
import com.placement.portal.entity.Job;
import com.placement.portal.exception.ApiException;
import com.placement.portal.repository.CompanyRepository;
import com.placement.portal.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;

    // Finds the logged-in company by the email stored inside their JWT token
    private Company getCompanyByEmail(String email) {
        return companyRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("Company not found"));
    }

    public JobResponse createJob(String companyEmail, JobRequest request) {
        Company company = getCompanyByEmail(companyEmail);

        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setRequiredSkills(request.getRequiredSkills());
        job.setLocation(request.getLocation());
        job.setJobType(request.getJobType());
        job.setSalary(request.getSalary());
        job.setCompanyId(company.getId());
        job.setCompanyName(company.getCompanyName());
        job.setStatus("OPEN");

        Job saved = jobRepository.save(job);
        return toResponse(saved);
    }

    // Returns ONLY the jobs posted by the currently logged-in company
    public List<JobResponse> getMyJobs(String companyEmail) {
        Company company = getCompanyByEmail(companyEmail);
        return jobRepository.findByCompanyId(company.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ===================== Module 5 addition =====================
    // Returns ALL jobs with status OPEN -- used by students browsing available jobs.
    public List<JobResponse> getAllOpenJobs() {
        return jobRepository.findAll()
                .stream()
                .filter(job -> "OPEN".equals(job.getStatus()))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public JobResponse updateJob(String companyEmail, Long jobId, JobRequest request) {
        Company company = getCompanyByEmail(companyEmail);
        Job job = getJobOwnedByCompany(jobId, company.getId());

        if (request.getTitle() != null) job.setTitle(request.getTitle());
        if (request.getDescription() != null) job.setDescription(request.getDescription());
        if (request.getRequiredSkills() != null) job.setRequiredSkills(request.getRequiredSkills());
        if (request.getLocation() != null) job.setLocation(request.getLocation());
        if (request.getJobType() != null) job.setJobType(request.getJobType());
        if (request.getSalary() != null) job.setSalary(request.getSalary());

        Job saved = jobRepository.save(job);
        return toResponse(saved);
    }

    public void deleteJob(String companyEmail, Long jobId) {
        Company company = getCompanyByEmail(companyEmail);
        Job job = getJobOwnedByCompany(jobId, company.getId());
        jobRepository.delete(job);
    }

    // SECURITY CHECK: makes sure a company can only touch jobs IT posted.
    // Without this, Company A could update/delete Company B's job just by
    // guessing a job ID in the URL -- this method prevents that.
    private Job getJobOwnedByCompany(Long jobId, Long companyId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ApiException("Job not found"));

        if (!job.getCompanyId().equals(companyId)) {
            throw new ApiException("You are not authorized to modify this job");
        }
        return job;
    }

    private JobResponse toResponse(Job j) {
        return new JobResponse(
                j.getId(),
                j.getTitle(),
                j.getDescription(),
                j.getRequiredSkills(),
                j.getLocation(),
                j.getJobType(),
                j.getSalary(),
                j.getCompanyId(),
                j.getCompanyName(),
                j.getStatus(),
                j.getPostedDate()
        );
    }
}