package com.placement.portal.storage;

import com.placement.portal.exception.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Handles saving uploaded resume files to a folder on disk
 * (NOT into the database -- storing large files in a database is bad practice).
 * We only store the FILENAME in the database (see Student.resumeFileName);
 * the actual PDF bytes live in the "uploads/resumes" folder.
 */
@Service
public class FileStorageService {

    // This folder will be created automatically, right next to where your app runs.
    private final Path uploadDir = Paths.get("uploads/resumes");

    public FileStorageService() {
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public String storeResume(Long studentId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new ApiException("Please select a file to upload");
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.toLowerCase().endsWith(".pdf")) {
            throw new ApiException("Only PDF files are allowed for resumes");
        }

        // Limit file size to 5MB
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new ApiException("Resume file must be smaller than 5MB");
        }

        // Name the file using the student's ID so each student has a unique, predictable filename
        // e.g. "3_resume.pdf" for student with id=3. This also means re-uploading replaces the old one.
        String storedFileName = studentId + "_resume.pdf";

        try {
            Path targetPath = uploadDir.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ApiException("Failed to save resume file: " + e.getMessage());
        }

        return storedFileName;
    }

    public Path getResumePath(String fileName) {
        return uploadDir.resolve(fileName);
    }
}