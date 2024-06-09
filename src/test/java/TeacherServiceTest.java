import com.project.dto.request.teacher.TeacherRequestDto;
import com.project.dto.response.security.JwtDto;
import com.project.dto.response.security.UserSessionDto;
import com.project.dto.response.teacher.TeacherResponseDto;
import com.project.entity.Teacher;
import com.project.exception.teacher.TeacherAlreadyExistsException;
import com.project.exception.teacher.TeacherNotFoundException;
import com.project.repository.TeacherRepository;
import com.project.security.JwtProvider;
import com.project.service.TeacherService;
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
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private TeacherService teacherService;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserSessionDto userSessionDto;
    private Teacher user;
    private String jwt;



    @Test
    public void testCreateTeacher_Success() {
        //Given
        TeacherRequestDto teacherRequestDto = new TeacherRequestDto();
        teacherRequestDto.setUsername("testUser");

        Teacher teacher = new Teacher();
        teacher.setUsername("testUser");

        TeacherResponseDto teacherResponseDto = new TeacherResponseDto();
        teacherResponseDto.setUsername("testUser");

        //WHEN
        when(teacherRepository.findById("testUser")).thenReturn(Optional.empty());
        when(modelMapper.map(teacherRequestDto, Teacher.class)).thenReturn(teacher);
        when(teacherRepository.save(teacher)).thenReturn(teacher);
        when(modelMapper.map(teacher, TeacherResponseDto.class)).thenReturn(teacherResponseDto);

        TeacherResponseDto result = teacherService.createTeacher(teacherRequestDto);

        //THEN
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(teacherRepository).findById("testUser");
        verify(teacherRepository).save(teacher);
    }


    @Test
    void testCreateTeacher_TeacherAlreadyExists() {
        //GIVEN
        TeacherRequestDto teacherRequestDto = new TeacherRequestDto();
        teacherRequestDto.setUsername("existingTeacher");

        Teacher existingTeacher = new Teacher();
        existingTeacher.setUsername("existingTeacher");

        //WHEN
        when(teacherRepository.findById("existingTeacher")).thenReturn(Optional.of(existingTeacher));

        //THEN
        assertThrows(TeacherAlreadyExistsException.class, () -> {
            teacherService.createTeacher(teacherRequestDto);
        });
    }

    @Test
    void testGetTeacher() {
        //GIVEN
        String teacherId = "daniel";
        Teacher teacher=new Teacher();

        //WHEN
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        TeacherResponseDto teacherResponseDto = new TeacherResponseDto();
        when(modelMapper.map(teacher, TeacherResponseDto.class)).thenReturn(teacherResponseDto);

        TeacherResponseDto result = teacherService.getTeacher(teacherId);

        //THEN
        assertNotNull(result);
        verify(teacherRepository).findById(teacherId);
    }
    @BeforeEach
    public void setUp() {
        userSessionDto = new UserSessionDto("username", "password");
        user = new Teacher();
        jwt = "generated-jwt";
    }

    @Test
    public void createSession_success() {
        when(teacherRepository.findById(userSessionDto.getUsername())).thenReturn(Optional.of(user));
        when(jwtProvider.generateJwt(user)).thenReturn(jwt);

        JwtDto result = teacherService.createSession(userSessionDto);

        assertNotNull(result);
        assertEquals(jwt, result.getJwt());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(teacherRepository).findById(userSessionDto.getUsername());
        verify(jwtProvider).generateJwt(user);
    }

    @Test
    public void createSession_userNotFound() {
        when(teacherRepository.findById(userSessionDto.getUsername())).thenReturn(Optional.empty());

        assertThrows(TeacherNotFoundException.class, () -> teacherService.createSession(userSessionDto));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(teacherRepository).findById(userSessionDto.getUsername());
        verify(jwtProvider, never()).generateJwt(any());
    }

    @Test
    void createSession_AuthenticationFails() {
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> teacherService.createSession(userSessionDto));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(teacherRepository, never()).findById(anyString());
        verify(jwtProvider, never()).generateJwt(any());
    }

}
