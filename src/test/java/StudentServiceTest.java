import com.project.dto.request.student.StudentRequestDto;
import com.project.dto.response.security.JwtDto;
import com.project.dto.response.security.UserSessionDto;
import com.project.dto.response.student.StudentResponseDto;
import com.project.entity.Student;
import com.project.exception.student.StudentAlreadyExistsException;
import com.project.exception.student.StudentNotFoundException;
import com.project.repository.StudentRepository;
import com.project.security.JwtProvider;
import com.project.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private StudentService studentService;

    private UserSessionDto userSessionDto;
    private Student student;
    private String jwt;

    @Test
    public void testCreateStudent_Success() {
        //GIVEN
        StudentRequestDto studentRequestDto = new StudentRequestDto();
        studentRequestDto.setUsername("testUser");

        Student student = new Student();
        student.setUsername("testUser");

        StudentResponseDto studentResponseDto = new StudentResponseDto();
        studentResponseDto.setUsername("testUser");

        //WHEN
        when(studentRepository.findById("testUser")).thenReturn(Optional.empty());
        when(modelMapper.map(studentRequestDto, Student.class)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);
        when(modelMapper.map(student, StudentResponseDto.class)).thenReturn(studentResponseDto);
        StudentResponseDto result = studentService.createStudent(studentRequestDto);

        //THEN
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(studentRepository).findById("testUser");
        verify(studentRepository).save(student);
    }

    @Test
    void testCreateStudent_StudentAlreadyExists() {
        //GIVEN
        StudentRequestDto studentRequestDto = new StudentRequestDto();
        studentRequestDto.setUsername("existingStudent");

        Student existingStudent = new Student();
        existingStudent.setUsername("existingStudent");

        //WHEN
        when(studentRepository.findById("existingStudent")).thenReturn(Optional.of(existingStudent));

        //THEN
        assertThrows(StudentAlreadyExistsException.class, () -> {
            studentService.createStudent(studentRequestDto);
        });}

    @Test
    void testGetStudent() {
        //GIVEN
        String studentId = "dan";
        Student student=new Student();

        //WHEN
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        StudentResponseDto studentResponseDto = new StudentResponseDto();
        when(modelMapper.map(student, StudentResponseDto.class)).thenReturn(studentResponseDto);

        StudentResponseDto result = studentService.getStudent(studentId);

        //THEN
        assertNotNull(result);
        verify(studentRepository).findById(studentId);
    }

    @Test
    public void testLoadStudent_NonExistingUsername() {
        //GIVEN
        String username = "nonExistingUsername";

        //WHEN
        when(studentRepository.findById(username)).thenReturn(Optional.empty());

        //THEN
        assertThrows(StudentNotFoundException.class, () -> studentService.loadStudent(username));
    }
    @BeforeEach
    void setUp() {
        userSessionDto = new UserSessionDto();
        userSessionDto.setUsername("testUser");
        userSessionDto.setPassword("testPassword");

        student = new Student();
        student.setUsername("testUser");

        jwt = "generatedJwt";
    }

    @Test
    void createSession_Success() {
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(mock(Authentication.class));
        when(studentRepository.findById("testUser")).thenReturn(Optional.of(student));
        when(jwtProvider.generateJwt(student)).thenReturn(jwt);

        JwtDto result = studentService.createSession(userSessionDto);

        assertNotNull(result);
        assertEquals(jwt, result.getJwt());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(studentRepository).findById("testUser");
        verify(jwtProvider).generateJwt(student);
    }

    @Test
    void createSession_AuthenticationFails() {
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> studentService.createSession(userSessionDto));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(studentRepository, never()).findById(anyString());
        verify(jwtProvider, never()).generateJwt(any());
    }

    @Test
    void createSession_UserNotFound() {
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(mock(Authentication.class));
        when(studentRepository.findById("testUser")).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.createSession(userSessionDto));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(studentRepository).findById("testUser");
        verify(jwtProvider, never()).generateJwt(any());
    }

    }

