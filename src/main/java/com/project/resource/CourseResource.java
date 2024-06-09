package com.project.resource;

import com.project.dto.request.course.CourseRequestDto;
import com.project.dto.request.course.WithdrawalRequestDto;
import com.project.dto.request.enrollment.EnrollmentActionRequestDto;
import com.project.dto.request.grade.GradeRequestDto;
import com.project.dto.response.course.CourseListResponseDto;
import com.project.dto.response.course.CourseResponseDto;
import com.project.dto.response.enrollment.EnrollmentStatusResponseDto;
import com.project.dto.response.enrollment.EnrollmentStudentListResponseDto;
import com.project.dto.response.grade.GradeResponseDto;
import com.project.service.CourseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/courses")
public class CourseResource {

    private CourseService courseService;

    @PostMapping(path = "/me")
    @Secured({"TEACHER"})
    public CourseResponseDto create(@RequestBody @Valid CourseRequestDto courseRequestDto){
        return courseService.createCourse(courseRequestDto);
    }

    @GetMapping(path="/{id}")
    public CourseResponseDto get(@PathVariable Long id){
        return courseService.getCourse(id);
    }

    @GetMapping
    public CourseListResponseDto getAll(){
        return courseService.getAll();
    }

    @Secured({"STUDENT"})
    @PostMapping(path = "/{id}/enrollments")
    public EnrollmentStatusResponseDto enrollStudent(@PathVariable Long id){
        return courseService.createEnrollmentApplication(id);
    }

    @Secured({"TEACHER"})
    @PostMapping(path = "/{id}/enrollment-actions")
    public EnrollmentStatusResponseDto processEnrollmentAction(@PathVariable Long id, @RequestBody @Valid EnrollmentActionRequestDto enrollmentActionRequestDto) {
        return courseService.processEnrollmentAction(id, enrollmentActionRequestDto);
    }

    @Secured({"TEACHER"})
    @PostMapping(path = "{id}/enrollments/withdrawals")
    public EnrollmentStatusResponseDto withdrawStudent(@PathVariable Long id, @RequestBody @Valid WithdrawalRequestDto withdrawalRequestDto) {
        return courseService.withdrawStudent(id, withdrawalRequestDto);
    }

    @Secured({"TEACHER"})
    @PostMapping(path = "/{id}/grades")
    public GradeResponseDto gradeStudent(@PathVariable Long id, @RequestBody @Valid GradeRequestDto gradeRequestDto){
        return courseService.gradeStudent(id, gradeRequestDto);
    }

    @Secured({"TEACHER"})
    @GetMapping(path = "/{id}/enrollments")
    public EnrollmentStudentListResponseDto getEnrollmentsByStatus(@PathVariable Long id, @RequestParam("status") String status){
        return courseService.getEnrollmentsByStatus(id, status);
    }
}
