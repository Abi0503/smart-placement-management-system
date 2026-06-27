package com.placement.portal.config;

import com.placement.portal.entity.Admin;
import com.placement.portal.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Runs ONCE every time the application starts up.
 * If no admin account exists yet, it creates a default one:
 *   email:    admin@placementportal.com
 *   password: admin123
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setFullName("System Admin");
            admin.setEmail("admin@placementportal.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            adminRepository.save(admin);

            System.out.println("===========================================");
            System.out.println("  Default ADMIN account created:");
            System.out.println("  Email:    admin@placementportal.com");
            System.out.println("  Password: admin123");
            System.out.println("===========================================");
        }
    }
}
