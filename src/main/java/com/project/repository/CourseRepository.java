package com.project.repository;


import com.project.entity.Course;
import com.project.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByNameAndTeacher (String name, Teacher teacher);
}
