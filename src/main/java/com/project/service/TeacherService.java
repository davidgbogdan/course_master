package com.project.service;

import com.project.dto.request.teacher.TeacherRequestDto;
import com.project.dto.response.course.CourseListResponseDto;
import com.project.dto.response.course.CourseResponseDto;
import com.project.dto.response.security.JwtDto;
import com.project.dto.response.security.UserSessionDto;
import com.project.dto.response.teacher.TeacherListResponseDto;
import com.project.dto.response.teacher.TeacherResponseDto;
import com.project.entity.Teacher;
import com.project.exception.teacher.TeacherAlreadyExistsException;
import com.project.exception.teacher.TeacherNotFoundException;
import com.project.repository.CourseRepository;
import com.project.repository.TeacherRepository;
import com.project.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TeacherService {
    private TeacherRepository teacherRepository;
    private CourseRepository courseRepository;
    private ModelMapper modelMapper;
    private JwtProvider jwtProvider;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    public TeacherResponseDto createTeacher(TeacherRequestDto teacherRequestDto){
        teacherRepository.findById(teacherRequestDto.getUsername())
                .ifPresent(teacher -> {throw new TeacherAlreadyExistsException();});

        var teacher = modelMapper.map(teacherRequestDto, Teacher.class);
        teacher.setPassword(passwordEncoder.encode(teacherRequestDto.getPassword()));

        var savedEntity = teacherRepository.save(teacher);

        return modelMapper.map(savedEntity, TeacherResponseDto.class);
    }

    public TeacherResponseDto getTeacher(String username) {
        var teacher = loadTeacher(username);
        return modelMapper.map(teacher, TeacherResponseDto.class);
    }

    public Teacher loadTeacher(String username){
        return teacherRepository.findById(username).orElseThrow(TeacherNotFoundException::new);
    }

    public TeacherListResponseDto getAll() {
        return new TeacherListResponseDto(
          teacherRepository.findAll()
                  .stream()
                  .map(teacher -> modelMapper.map(teacher, TeacherResponseDto.class))
                  .toList()
        );
    }

    public CourseListResponseDto getCourses(String username) {
        return new CourseListResponseDto(
                courseRepository.findAll()
                        .stream()
                        .filter(course -> course.getTeacher().equals(loadTeacher(username)))
                        .map(course -> modelMapper.map(course, CourseResponseDto.class))
                        .toList()
        );
    }

    public JwtDto createSession(UserSessionDto userSessionDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userSessionDto.getUsername(),
                userSessionDto.getPassword()
        ));

        var user = teacherRepository.findById(userSessionDto.getUsername())
                .orElseThrow(TeacherNotFoundException::new);

        String jwt = jwtProvider.generateJwt(user);

        return new JwtDto(jwt);
    }
}
