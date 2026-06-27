package com.placement.portal.service;

import com.placement.portal.dto.ApplicationResponse;
import com.placement.portal.entity.Application;
import com.placement.portal.entity.Company;
import com.placement.portal.entity.Job;
import com.placement.portal.entity.Student;
import com.placement.portal.exception.ApiException;
import com.placement.portal.repository.ApplicationRepository;
import com.placement.portal.repository.CompanyRepository;
import com.placement.portal.repository.JobRepository;
import com.placement.portal.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final NotificationService notificationService;

    private Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("Student not found"));
    }

    private Company getCompanyByEmail(String email) {
        return companyRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("Company not found"));
    }

    // ===================== Student actions =====================

    public ApplicationResponse applyToJob(String studentEmail, Long jobId) {
        Student student = getStudentByEmail(studentEmail);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ApiException("Job not found"));

        if (!"OPEN".equals(job.getStatus())) {
            throw new ApiException("This job is no longer accepting applications");
        }

        // Prevent applying twice to the same job
        applicationRepository.findByStudentIdAndJobId(student.getId(), jobId)
                .ifPresent(a -> {
                    throw new ApiException("You have already applied to this job");
                });

        Application application = new Application();
        application.setStudentId(student.getId());
        application.setJobId(job.getId());
        application.setStudentName(student.getFullName());
        application.setStudentEmail(student.getEmail());
        application.setJobTitle(job.getTitle());
        application.setCompanyName(job.getCompanyName());
        application.setCompanyId(job.getCompanyId());
        application.setStatus("APPLIED");

        Application saved = applicationRepository.save(application);

        // Notify the company that a new application has come in
        companyRepository.findById(job.getCompanyId()).ifPresent(company ->
                notificationService.notify(
                        company.getEmail(),
                        "COMPANY",
                        "New Application Received",
                        student.getFullName() + " applied for " + job.getTitle(),
                        "NEW_APPLICATION"
                )
        );

        return toResponse(saved);
    }
    

    // Returns all applications made by the currently logged-in student
    public List<ApplicationResponse> getMyApplications(String studentEmail) {
        Student student = getStudentByEmail(studentEmail);
        return applicationRepository.findByStudentId(student.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ===================== Company actions =====================

    // Returns all applicants for a specific job -- but ONLY if that job
    // belongs to the company making the request (authorization check).
    public List<ApplicationResponse> getApplicantsForJob(String companyEmail, Long jobId) {
        Company company = getCompanyByEmail(companyEmail);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ApiException("Job not found"));

        if (!job.getCompanyId().equals(company.getId())) {
            throw new ApiException("You are not authorized to view applicants for this job");
        }

        return applicationRepository.findByJobId(jobId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Updates an applicant's status -- ONLY the company that owns the job can do this.
    public ApplicationResponse updateApplicationStatus(String companyEmail, Long applicationId, String newStatus) {
        Company company = getCompanyByEmail(companyEmail);

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApiException("Application not found"));

        if (!application.getCompanyId().equals(company.getId())) {
            throw new ApiException("You are not authorized to update this application");
        }

        application.setStatus(newStatus.toUpperCase());
        Application saved = applicationRepository.save(application);

        // Notify the student that their application status has changed
        notificationService.notify(
                application.getStudentEmail(),
                "STUDENT",
                "Application Status Updated",
                "Your application for " + application.getJobTitle() + " at " + application.getCompanyName()
                        + " is now " + newStatus.toUpperCase(),
                "APPLICATION_STATUS"
        );

        return toResponse(saved);
    }
    

    private ApplicationResponse toResponse(Application a) {
        return new ApplicationResponse(
                a.getId(),
                a.getStudentId(),
                a.getJobId(),
                a.getStudentName(),
                a.getStudentEmail(),
                a.getJobTitle(),
                a.getCompanyName(),
                a.getCompanyId(),
                a.getStatus(),
                a.getAppliedDate()
        );
    }
}