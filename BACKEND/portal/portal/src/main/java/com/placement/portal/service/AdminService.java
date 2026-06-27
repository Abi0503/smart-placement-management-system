package com.placement.portal.service;

import com.placement.portal.dto.DashboardStatsResponse;
import com.placement.portal.dto.StudentProfileResponse;
import com.placement.portal.dto.CompanyProfileResponse;
import com.placement.portal.dto.JobResponse;
import com.placement.portal.entity.Company;
import com.placement.portal.entity.Job;
import com.placement.portal.entity.Student;
import com.placement.portal.exception.ApiException;
import com.placement.portal.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;

    public DashboardStatsResponse getDashboardStats() {
        long totalStudents = studentRepository.count();
        long totalCompanies = companyRepository.count();
        long totalJobs = jobRepository.count();
        long openJobs = jobRepository.findAll().stream()
                .filter(j -> "OPEN".equals(j.getStatus()))
                .count();
        long totalApplications = applicationRepository.count();
        long totalInterviews = interviewRepository.count();
        long selectedCount = applicationRepository.findAll().stream()
                .filter(a -> "SELECTED".equals(a.getStatus()))
                .count();

        return new DashboardStatsResponse(
                totalStudents, totalCompanies, totalJobs, openJobs,
                totalApplications, totalInterviews, selectedCount
        );
    }

    public List<StudentProfileResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::toStudentResponse)
                .collect(Collectors.toList());
    }

    public List<CompanyProfileResponse> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(this::toCompanyResponse)
                .collect(Collectors.toList());
    }

    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(this::toJobResponse)
                .collect(Collectors.toList());
    }

    public void deleteStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ApiException("Student not found");
        }
        studentRepository.deleteById(studentId);
    }

    public void deleteCompany(Long companyId) {
        if (!companyRepository.existsById(companyId)) {
            throw new ApiException("Company not found");
        }
        companyRepository.deleteById(companyId);
    }

    public void deleteJob(Long jobId) {
        if (!jobRepository.existsById(jobId)) {
            throw new ApiException("Job not found");
        }
        jobRepository.deleteById(jobId);
    }

    private StudentProfileResponse toStudentResponse(Student s) {
        return new StudentProfileResponse(
                s.getId(), s.getFullName(), s.getEmail(), s.getPhoneNumber(), s.getDepartment(), s.getRole(),
                s.getCollegeName(), s.getBranch(), s.getTenthPercentage(), s.getTwelfthPercentage(),
                s.getCgpa(), s.getPassingYear(), s.getSkills(), s.getResumeFileName()
        );
    }

    private CompanyProfileResponse toCompanyResponse(Company c) {
        return new CompanyProfileResponse(
                c.getId(), c.getCompanyName(), c.getEmail(), c.getWebsite(), c.getIndustry(),
                c.getDescription(), c.getContactPersonName(), c.getContactPhoneNumber(), c.getRole()
        );
    }

    private JobResponse toJobResponse(Job j) {
        return new JobResponse(
                j.getId(), j.getTitle(), j.getDescription(), j.getRequiredSkills(), j.getLocation(),
                j.getJobType(), j.getSalary(), j.getCompanyId(), j.getCompanyName(), j.getStatus(), j.getPostedDate()
        );
    }
}