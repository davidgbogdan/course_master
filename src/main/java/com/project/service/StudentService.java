package com.project.service;

import com.project.dto.request.student.StudentRequestDto;
import com.project.dto.response.enrollment.EnrollmentCourseListResponseDto;
import com.project.dto.response.enrollment.EnrollmentCourseResponseDto;
import com.project.dto.response.security.JwtDto;
import com.project.dto.response.security.UserSessionDto;
import com.project.dto.response.student.StudentListResponseDto;
import com.project.dto.response.student.StudentResponseDto;
import com.project.entity.Enrollment;
import com.project.entity.EnrollmentStatus;
import com.project.entity.Student;
import com.project.exception.enrollment.EnrollmentIllegalActionException;
import com.project.exception.student.StudentAlreadyExistsException;
import com.project.exception.student.StudentNotFoundException;
import com.project.repository.EnrollmentRepository;
import com.project.repository.StudentRepository;
import com.project.security.JwtProvider;
import com.project.security.SecurityUserDetails;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
@AllArgsConstructor
public class StudentService {
    private StudentRepository studentRepository;
    private EnrollmentRepository enrollmentRepository;
    private ModelMapper modelMapper;
    private JwtProvider jwtProvider;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    public StudentResponseDto createStudent(StudentRequestDto studentRequestDto){
        studentRepository.findById(studentRequestDto.getUsername())
                .ifPresent(student -> {throw new StudentAlreadyExistsException();});

        var student = modelMapper.map(studentRequestDto, Student.class);
        student.setPassword(passwordEncoder.encode(studentRequestDto.getPassword()));

        var savedEntity = studentRepository.save(student);

        return modelMapper.map(savedEntity, StudentResponseDto.class);
    }

    public StudentResponseDto getStudent(String username) {
        var student = loadStudent(username);
        return modelMapper.map(student, StudentResponseDto.class);
    }

    public StudentListResponseDto getAll() {
        return new StudentListResponseDto(
                studentRepository.findAll()
                        .stream()
                        .map(student -> modelMapper.map(student, StudentResponseDto.class))
                        .toList()
        );
    }

    public Student loadStudent(String username) {
        return studentRepository.findById(username).orElseThrow(StudentNotFoundException::new);
    }

    public EnrollmentCourseListResponseDto getEnrollmentsByStatus(String status) {
        var securityUserDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var student = loadStudent(securityUserDetails.getUsername());

        List<Enrollment> enrollments;
        Predicate<Enrollment> studentPredicate = enrollment -> enrollment.getStudent().equals(student);

        if (status == null || status.isBlank()) {
            enrollments = enrollmentRepository.findAll()
                    .stream()
                    .filter(studentPredicate)
                    .toList();
        } else {
            EnrollmentStatus enrollmentStatus;
            try {
                enrollmentStatus = EnrollmentStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new EnrollmentIllegalActionException();
            }

            Predicate<Enrollment> statusPredicate = enrollment -> enrollment.getStatus().equals(enrollmentStatus);

            enrollments = enrollmentRepository.findAll()
                    .stream()
                    .filter(studentPredicate.and(statusPredicate))
                    .toList();
        }

        return new EnrollmentCourseListResponseDto(
                enrollments.stream()
                        .map(enrollment -> modelMapper.map(enrollment, EnrollmentCourseResponseDto.class))
                        .toList()
        );
    }

    public JwtDto createSession(UserSessionDto userSessionDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userSessionDto.getUsername(),
                userSessionDto.getPassword()
        ));

        var user = studentRepository.findById(userSessionDto.getUsername())
                .orElseThrow(StudentNotFoundException::new);

        String jwt = jwtProvider.generateJwt(user);

        return new JwtDto(jwt);
    }
}
