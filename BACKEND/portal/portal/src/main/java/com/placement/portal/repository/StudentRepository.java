package com.placement.portal.repository;

import com.placement.portal.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Just by extending JpaRepository, we instantly get methods like:
 * save(), findById(), findAll(), deleteById(), etc. -- no SQL needed.
 *
 * findByEmail() below is a "derived query method": Spring reads the
 * method name and automatically writes the SQL
 * (SELECT * FROM students WHERE email = ?) for us.
 */
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);
}