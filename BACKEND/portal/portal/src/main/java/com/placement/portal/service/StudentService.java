package com.placement.portal.service;

import com.placement.portal.dto.ProfileUpdateRequest;
import com.placement.portal.dto.StudentProfileResponse;
import com.placement.portal.entity.Student;
import com.placement.portal.exception.ApiException;
import com.placement.portal.repository.StudentRepository;
import com.placement.portal.storage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final FileStorageService fileStorageService;

    // Finds the logged-in student by the email stored inside their JWT token
    private Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("Student not found"));
    }

    public StudentProfileResponse getProfile(String email) {
        Student student = getStudentByEmail(email);
        return toResponse(student);
    }

    public StudentProfileResponse updateProfile(String email, ProfileUpdateRequest request) {
        Student student = getStudentByEmail(email);

        // Only update fields that were actually provided -- keeps this flexible
        if (request.getFullName() != null) student.setFullName(request.getFullName());
        if (request.getPhoneNumber() != null) student.setPhoneNumber(request.getPhoneNumber());
        if (request.getDepartment() != null) student.setDepartment(request.getDepartment());
        if (request.getCollegeName() != null) student.setCollegeName(request.getCollegeName());
        if (request.getBranch() != null) student.setBranch(request.getBranch());
        if (request.getTenthPercentage() != null) student.setTenthPercentage(request.getTenthPercentage());
        if (request.getTwelfthPercentage() != null) student.setTwelfthPercentage(request.getTwelfthPercentage());
        if (request.getCgpa() != null) student.setCgpa(request.getCgpa());
        if (request.getPassingYear() != null) student.setPassingYear(request.getPassingYear());
        if (request.getSkills() != null) student.setSkills(request.getSkills());

        Student saved = studentRepository.save(student);
        return toResponse(saved);
    }

    public StudentProfileResponse uploadResume(String email, MultipartFile file) {
        Student student = getStudentByEmail(email);

        String storedFileName = fileStorageService.storeResume(student.getId(), file);
        student.setResumeFileName(storedFileName);

        Student saved = studentRepository.save(student);
        return toResponse(saved);
    }

    // Converts the database Entity into the DTO we actually send to the frontend.
    // We never send the password back, even hashed -- it's simply not included here.
    private StudentProfileResponse toResponse(Student s) {
        return new StudentProfileResponse(
                s.getId(),
                s.getFullName(),
                s.getEmail(),
                s.getPhoneNumber(),
                s.getDepartment(),
                s.getRole(),
                s.getCollegeName(),
                s.getBranch(),
                s.getTenthPercentage(),
                s.getTwelfthPercentage(),
                s.getCgpa(),
                s.getPassingYear(),
                s.getSkills(),
                s.getResumeFileName()
        );
    }
}