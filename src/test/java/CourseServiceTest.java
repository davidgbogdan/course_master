import com.project.dto.request.course.CourseRequestDto;
import com.project.dto.request.schedule.ScheduleRequestDto;
import com.project.dto.response.course.CourseResponseDto;
import com.project.entity.*;
import com.project.exception.course.CourseNotFoundException;
import com.project.exception.teacher.TeacherNotFoundException;
import com.project.repository.CourseRepository;
import com.project.repository.TeacherRepository;
import com.project.security.SecurityUserDetails;
import com.project.service.CourseService;
import com.project.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CourseServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityUserDetails securityUserDetails;

    @InjectMocks
    private TeacherService teacherService;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void testCreateCourse() {
        SecurityUserDetails userDetails = mock(SecurityUserDetails.class);
        when(userDetails.getUsername()).thenReturn("teacher1");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        //GIVEN
        ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto();
        CourseRequestDto courseRequestDto = new CourseRequestDto();
        courseRequestDto.setSchedules(Collections.singletonList(scheduleRequestDto));

        //WHEN
        Teacher teacher = new Teacher();
        when(teacherRepository.findById("teacher1")).thenReturn(Optional.of(teacher));

        Course course = new Course();
        when(modelMapper.map(courseRequestDto, Course.class)).thenReturn(course);

        Schedule schedule = new Schedule();
        when(modelMapper.map(scheduleRequestDto, Schedule.class)).thenReturn(schedule);

        Course savedCourse = new Course();
        when(courseRepository.save(course)).thenReturn(savedCourse);

        CourseResponseDto courseResponseDto = new CourseResponseDto();
        when(modelMapper.map(savedCourse, CourseResponseDto.class)).thenReturn(courseResponseDto);

        CourseResponseDto result = courseService.createCourse(courseRequestDto);

        //THEN
        assertNotNull(result);
        verify(teacherRepository).findById("teacher1");
        verify(courseRepository).save(course);
    }

    @Test
    void testCreateCourse_nullCourseRequestDto() {
        assertThrows(NullPointerException.class, () -> {
            courseService.createCourse(null);
        });
    }

    @Test
    void testGetCourse() {
        //GIVEN
        Long courseId = 1L;
        Course course = new Course();

        //WHEN
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        CourseResponseDto courseResponseDto = new CourseResponseDto();
        when(modelMapper.map(course, CourseResponseDto.class)).thenReturn(courseResponseDto);

        CourseResponseDto result = courseService.getCourse(courseId);

        //THEN
        assertNotNull(result);
        verify(courseRepository).findById(courseId);
    }

    @Test
    public void testGetCourseNotFound() {
        //Given
        Long courseId = 1L;

        //When
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CourseNotFoundException.class, () -> {
            courseService.getCourse(courseId);
        });
        //Then
        assertEquals("COURSE_NOT_FOUND", exception.getMessage());

        verify(courseRepository, times(1)).findById(courseId);
        verify(modelMapper, times(0)).map(any(Course.class), any(CourseResponseDto.class));
    }

    @Test
    public void testLoadCourse_NonExistingCourse() {
        //GIVEN
        Long id = 1L;

        //WHEN
        when(courseRepository.findById(id)).thenReturn(Optional.empty());

        //THEN
        assertThrows(CourseNotFoundException.class, () -> courseService.loadCourse(id));
    }

    @Test
    void testLoadTeacher_teacherFound() {
        // Given
        String username = "existingTeacher";
        Teacher expectedTeacher = new Teacher();
        expectedTeacher.setUsername(username);

        when(teacherRepository.findById(username)).thenReturn(Optional.of(expectedTeacher));

        // When
        Teacher actualTeacher = teacherService.loadTeacher(username);

        // Then
        assertEquals(expectedTeacher.getUsername(), actualTeacher.getUsername());
        verify(teacherRepository).findById(username);
    }

    @Test
    void testLoadTeacher_teacherNotFound() {
        // Given
        String username = "nonExistentTeacher";
        //When
        when(teacherRepository.findById(username)).thenReturn(Optional.empty());

        //Then
        assertThrows(TeacherNotFoundException.class, () -> {
            teacherService.loadTeacher(username);
        });
        verify(teacherRepository).findById(username);
    }
}

