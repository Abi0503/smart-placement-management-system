package com.placement.portal.security;

import com.placement.portal.entity.Admin;
import com.placement.portal.entity.Company;
import com.placement.portal.entity.Student;
import com.placement.portal.repository.AdminRepository;
import com.placement.portal.repository.CompanyRepository;
import com.placement.portal.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 * Spring Security doesn't know what a "Student", "Company", or "Admin" is.
 * This class translates: "given an email, find the matching account in
 * EITHER the students, companies, OR admins table, and wrap it into the
 * UserDetails object Spring Security understands."
 *
 * We check Student first, then Company, then Admin.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            return new User(
                    student.getEmail(),
                    student.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + student.getRole()))
            );
        }

        Optional<Company> companyOpt = companyRepository.findByEmail(email);
        if (companyOpt.isPresent()) {
            Company company = companyOpt.get();
            return new User(
                    company.getEmail(),
                    company.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + company.getRole()))
            );
        }

        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            return new User(
                    admin.getEmail(),
                    admin.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + admin.getRole()))
            );
        }

        throw new UsernameNotFoundException("No account found with email: " + email);
    }
}