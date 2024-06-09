package com.project.resource;

import com.project.dto.request.teacher.TeacherRequestDto;
import com.project.dto.response.course.CourseListResponseDto;
import com.project.dto.response.security.JwtDto;
import com.project.dto.response.security.UserSessionDto;
import com.project.dto.response.teacher.TeacherListResponseDto;
import com.project.dto.response.teacher.TeacherResponseDto;
import com.project.service.TeacherService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/teachers")
public class TeacherResource {
    private TeacherService teacherService;

    @PostMapping
    public TeacherResponseDto create(@RequestBody @Valid TeacherRequestDto teacher){
        return teacherService.createTeacher(teacher);
    }

    @GetMapping(path = "/{username}")
    public TeacherResponseDto get(@PathVariable String username) {
        return teacherService.getTeacher(username);
    }

    @GetMapping
    public TeacherListResponseDto getAll(){
        return teacherService.getAll();
    }

    @GetMapping(path = "/{username}/courses")
    public CourseListResponseDto getCourses(@PathVariable String username){
        return teacherService.getCourses(username);
    }

    @PostMapping(path = "/sessions")
    public JwtDto createSession(@RequestBody @Valid UserSessionDto userSessionDto){
        return teacherService.createSession(userSessionDto);
    }
}
