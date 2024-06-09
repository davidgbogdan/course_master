package com.project.resource;

import com.project.dto.request.student.StudentRequestDto;
import com.project.dto.response.enrollment.EnrollmentCourseListResponseDto;
import com.project.dto.response.security.JwtDto;
import com.project.dto.response.security.UserSessionDto;
import com.project.dto.response.student.StudentListResponseDto;
import com.project.dto.response.student.StudentResponseDto;
import com.project.service.StudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/students")
public class StudentResource {
    private StudentService studentService;

    @PostMapping
    public StudentResponseDto create(@RequestBody @Valid StudentRequestDto student) {
        return studentService.createStudent(student);
    }

    @GetMapping(path = "/{username}")
    public StudentResponseDto get(@PathVariable String username){
        return studentService.getStudent(username);
    }

    @GetMapping
    public StudentListResponseDto getAll() {
        return studentService.getAll();
    }

    @Secured({"STUDENT"})
    @GetMapping(path = "/me/enrollments")
    public EnrollmentCourseListResponseDto getEnrollmentsByStatus(@RequestParam("status") String status) {
        return studentService.getEnrollmentsByStatus(status);
    }

    @PostMapping(path = "/sessions")
    public JwtDto createSession(@RequestBody @Valid UserSessionDto userSessionDto){
        return studentService.createSession(userSessionDto);
    }
}
