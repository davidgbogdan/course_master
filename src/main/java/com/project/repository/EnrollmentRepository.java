package com.project.repository;

import com.project.entity.Course;
import com.project.entity.Enrollment;
import com.project.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByCourseAndStudent(Course course, Student student);
}
