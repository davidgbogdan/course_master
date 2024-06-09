package com.project.service;

import com.project.dto.request.course.CourseRequestDto;
import com.project.dto.request.course.WithdrawalRequestDto;
import com.project.dto.request.enrollment.EnrollmentActionRequestDto;
import com.project.dto.request.grade.GradeRequestDto;
import com.project.dto.response.course.CourseListResponseDto;
import com.project.dto.response.course.CourseResponseDto;
import com.project.dto.response.enrollment.EnrollmentStatusResponseDto;
import com.project.dto.response.enrollment.EnrollmentStudentListResponseDto;
import com.project.dto.response.enrollment.EnrollmentStudentResponseDto;
import com.project.dto.response.grade.GradeResponseDto;
import com.project.entity.*;
import com.project.exception.course.CourseAlreadyExistsException;
import com.project.exception.course.CourseFullException;
import com.project.exception.course.CourseNotFoundException;
import com.project.exception.enrollment.EnrollmentAlreadyExistsException;
import com.project.exception.enrollment.EnrollmentIllegalActionException;
import com.project.exception.enrollment.EnrollmentNotFoundException;
import com.project.exception.teacher.TeacherInvalidActionException;
import com.project.exception.teacher.TeacherNotFoundException;
import com.project.security.SecurityUserDetails;
import com.project.repository.CourseRepository;
import com.project.repository.EnrollmentRepository;
import com.project.repository.StudentRepository;
import com.project.repository.TeacherRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@AllArgsConstructor
public class CourseService {
    private CourseRepository courseRepository;
    private StudentRepository studentRepository;
    private TeacherRepository teacherRepository;
    private EnrollmentRepository enrollmentRepository;
    private ModelMapper modelMapper;
    private StudentService studentService;
    private ScheduleService scheduleService;

    public CourseResponseDto createCourse(CourseRequestDto courseRequestDto) {
        var securityUserDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var teacher = loadTeacher(securityUserDetails.getUsername());

        courseRepository.findByNameAndTeacher(courseRequestDto.getName(), teacher)
                .ifPresent(course -> {throw new CourseAlreadyExistsException();});

        var course = modelMapper.map(courseRequestDto, Course.class);
        teacher.addCourseToTeacher(course);

        courseRequestDto.getSchedules().forEach(scheduleRequestDto -> {
            var schedule = modelMapper.map(scheduleRequestDto, Schedule.class);
            course.addSchedule(schedule);
        });

        var savedCourseEntity = courseRepository.save(course);

        return modelMapper.map(savedCourseEntity, CourseResponseDto.class);
    }

    public CourseResponseDto getCourse(Long id) {
        var course = loadCourse(id);
        return modelMapper.map(course, CourseResponseDto.class);
    }

    public Course loadCourse(Long id) {
        return courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);
    }

    public CourseListResponseDto getAll() {
        return new CourseListResponseDto(courseRepository.findAll()
                .stream()
                .map(course -> modelMapper.map(course,CourseResponseDto.class))
                .toList()
        );
    }

    public EnrollmentStatusResponseDto createEnrollmentApplication(Long id) {
        var course = loadCourse(id);
        var securityUserDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var student = studentService.loadStudent(securityUserDetails.getUsername());

        Optional<Enrollment> existingEnrollment = enrollmentRepository.findByCourseAndStudent(course, student);
        if (existingEnrollment.isPresent()) {
            throw new EnrollmentAlreadyExistsException();
        } else {
            checkCourseIsFull(course);
            scheduleService.checkOverlappingSchedules(student, course);

            Enrollment enrollment = Enrollment.builder()
                    .course(course)
                    .student(student)
                    .build();

            course.addEnrollmentToCourse(enrollment, student);
            courseRepository.save(course);

            return modelMapper.map(enrollment, EnrollmentStatusResponseDto.class);
        }
    }

    public EnrollmentStatusResponseDto processEnrollmentAction(Long id, EnrollmentActionRequestDto enrollmentActionRequestDto) {
        var course = loadCourse(id);
        var securityUserDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var teacher = loadTeacher(securityUserDetails.getUsername());
        var student = studentService.loadStudent(enrollmentActionRequestDto.getStudentUsername());

        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByCourseAndStudent(course, student);

        if (enrollmentOpt.isPresent()) {
            Enrollment enrollment = enrollmentOpt.get();

            if (enrollment.getStatus() != EnrollmentStatus.APPROVED && "approve".equalsIgnoreCase(enrollmentActionRequestDto.getAction())) {
                checkCourseBelongsToTeacher(course, teacher);
                checkCourseIsFull(course);

                enrollment.approveEnrollment();
                course.incrementCurrentAttendees();

                courseRepository.save(course);
                studentRepository.save(student);

            } else if (enrollment.getStatus() != EnrollmentStatus.APPROVED && "deny".equalsIgnoreCase(enrollmentActionRequestDto.getAction())) {
                enrollment.denyEnrollment();
            } else {
                throw new EnrollmentIllegalActionException();
            }

            enrollmentRepository.save(enrollment);
            return modelMapper.map(enrollment, EnrollmentStatusResponseDto.class);
        } else {
            throw new EnrollmentNotFoundException();
        }
    }


    public Teacher loadTeacher(String username){
        return teacherRepository.findById(username).orElseThrow(TeacherNotFoundException::new);
    }

    public GradeResponseDto gradeStudent(Long id, GradeRequestDto gradeRequestDto) {
        var course = loadCourse(id);
        var securityUserDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var teacher = loadTeacher(securityUserDetails.getUsername());
        var student = studentService.loadStudent(gradeRequestDto.getStudentUsername());

        checkCourseBelongsToTeacher(course, teacher);

        var enrollment = enrollmentRepository.findByCourseAndStudent(course, student)
                .orElseThrow(EnrollmentNotFoundException::new);

        if(enrollment.getStatus() != EnrollmentStatus.APPROVED){
            throw new EnrollmentIllegalActionException();
        } else {
            enrollment.setFinalGrade(gradeRequestDto.getFinalGrade());
            var savedEntity = enrollmentRepository.save(enrollment);

            return modelMapper.map(savedEntity, GradeResponseDto.class);
        }
    }

    public EnrollmentStatusResponseDto withdrawStudent(Long id, WithdrawalRequestDto withdrawalRequestDto) {
        var course = loadCourse(id);
        var securityUserDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var teacher = loadTeacher(securityUserDetails.getUsername());
        var student = studentService.loadStudent(withdrawalRequestDto.getStudentUsername());

        checkCourseBelongsToTeacher(course, teacher);

        var enrollment = enrollmentRepository.findByCourseAndStudent(course, student)
                .orElseThrow(EnrollmentNotFoundException::new);

        if (enrollment.getStatus() != EnrollmentStatus.APPROVED){
            throw new EnrollmentIllegalActionException();
        } else {
            enrollment.denyEnrollment();
            course.decrementCurrentAttendees();
            enrollment.setFinalGrade(0);

            courseRepository.save(course);
            studentRepository.save(student);
            var savedEntity = enrollmentRepository.save(enrollment);

            return modelMapper.map(savedEntity, EnrollmentStatusResponseDto.class);
        }
    }

    public EnrollmentStudentListResponseDto getEnrollmentsByStatus(Long id, String status) {
        var course = loadCourse(id);

        List<Enrollment> enrollments;
        Predicate<Enrollment> coursePredicate = enrollment -> enrollment.getCourse().equals(course);

        if(status == null || status.isBlank()) {
            enrollments = enrollmentRepository.findAll()
                    .stream()
                    .filter(coursePredicate)
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
                    .filter(coursePredicate.and(statusPredicate))
                    .toList();
        }

        return new EnrollmentStudentListResponseDto(
                enrollments.stream()
                        .map(enrollment -> modelMapper.map(enrollment, EnrollmentStudentResponseDto.class))
                        .toList()
        );
    }

    private void checkCourseBelongsToTeacher(Course course, Teacher teacher) {
        if (!course.getTeacher().getUsername().equals(teacher.getUsername())) {
            throw new TeacherInvalidActionException();
        }
    }

    private void checkCourseIsFull(Course course){
        if(course.checkIfFull())
            throw new CourseFullException();
    }
}
