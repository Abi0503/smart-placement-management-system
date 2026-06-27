package com.placement.portal.service;

import com.placement.portal.dto.InterviewRequest;
import com.placement.portal.dto.InterviewResponse;
import com.placement.portal.entity.Application;
import com.placement.portal.entity.Company;
import com.placement.portal.entity.Interview;
import com.placement.portal.entity.Student;
import com.placement.portal.exception.ApiException;
import com.placement.portal.repository.ApplicationRepository;
import com.placement.portal.repository.CompanyRepository;
import com.placement.portal.repository.InterviewRepository;
import com.placement.portal.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final CompanyRepository companyRepository;
    private final StudentRepository studentRepository;
    private final NotificationService notificationService;

    private Company getCompanyByEmail(String email) {
        return companyRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("Company not found"));
    }

    private Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("Student not found"));
    }

    // ===================== Company actions =====================

    public InterviewResponse scheduleInterview(String companyEmail, Long applicationId, InterviewRequest request) {
        Company company = getCompanyByEmail(companyEmail);

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApiException("Application not found"));

        // SECURITY CHECK: a company can only schedule interviews for applications to ITS OWN jobs
        if (!application.getCompanyId().equals(company.getId())) {
            throw new ApiException("You are not authorized to schedule an interview for this application");
        }

        // Prevent scheduling two interviews for the same application
        interviewRepository.findByApplicationId(applicationId)
                .ifPresent(i -> {
                    throw new ApiException("An interview has already been scheduled for this application");
                });

        Interview interview = new Interview();
        interview.setApplicationId(application.getId());
        interview.setStudentId(application.getStudentId());
        interview.setStudentName(application.getStudentName());
        interview.setStudentEmail(application.getStudentEmail());
        interview.setJobId(application.getJobId());
        interview.setJobTitle(application.getJobTitle());
        interview.setCompanyId(application.getCompanyId());
        interview.setCompanyName(application.getCompanyName());
        interview.setScheduledAt(request.getScheduledAt());
        interview.setMode(request.getMode());
        interview.setLocationOrLink(request.getLocationOrLink());
        interview.setNotes(request.getNotes());
        interview.setStatus("SCHEDULED");
        interview.setResult("PENDING");

        // Also move the application status forward, since an interview being
        // scheduled implies the student has been shortlisted
        application.setStatus("SHORTLISTED");
        applicationRepository.save(application);

        Interview saved = interviewRepository.save(interview);

        // Notify the student that an interview has been scheduled
        notificationService.notify(
                application.getStudentEmail(),
                "STUDENT",
                "Interview Scheduled",
                "Your interview for " + application.getJobTitle() + " at " + application.getCompanyName()
                        + " is scheduled on " + request.getScheduledAt() + " (" + request.getMode() + ")",
                "INTERVIEW_SCHEDULED"
        );

        return toResponse(saved);
    }

    public List<InterviewResponse> getCompanyInterviews(String companyEmail) {
        Company company = getCompanyByEmail(companyEmail);
        return interviewRepository.findByCompanyId(company.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public InterviewResponse updateResult(String companyEmail, Long interviewId, String status, String result) {
        Company company = getCompanyByEmail(companyEmail);

        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new ApiException("Interview not found"));

        if (!interview.getCompanyId().equals(company.getId())) {
            throw new ApiException("You are not authorized to update this interview");
        }

        interview.setStatus(status.toUpperCase());
        interview.setResult(result.toUpperCase());

        // If the student passed, reflect that on the underlying application too
        if ("PASSED".equalsIgnoreCase(result)) {
            applicationRepository.findById(interview.getApplicationId())
                    .ifPresent(app -> {
                        app.setStatus("SELECTED");
                        applicationRepository.save(app);
                    });
        } else if ("FAILED".equalsIgnoreCase(result)) {
            applicationRepository.findById(interview.getApplicationId())
                    .ifPresent(app -> {
                        app.setStatus("REJECTED");
                        applicationRepository.save(app);
                    });
        }

        Interview saved = interviewRepository.save(interview);

        // Notify the student about the interview result/status update
        notificationService.notify(
                interview.getStudentEmail(),
                "STUDENT",
                "Interview Update",
                "Your interview for " + interview.getJobTitle() + " at " + interview.getCompanyName()
                        + " is now " + status.toUpperCase() + " with result " + result.toUpperCase(),
                "INTERVIEW_RESULT"
        );

        return toResponse(saved);
    }

    // ===================== Student actions =====================

    public List<InterviewResponse> getMyInterviews(String studentEmail) {
        Student student = getStudentByEmail(studentEmail);
        return interviewRepository.findByStudentId(student.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private InterviewResponse toResponse(Interview i) {
        return new InterviewResponse(
                i.getId(),
                i.getApplicationId(),
                i.getStudentId(),
                i.getStudentName(),
                i.getStudentEmail(),
                i.getJobId(),
                i.getJobTitle(),
                i.getCompanyId(),
                i.getCompanyName(),
                i.getScheduledAt(),
                i.getMode(),
                i.getLocationOrLink(),
                i.getNotes(),
                i.getStatus(),
                i.getResult()
        );
    }
}
