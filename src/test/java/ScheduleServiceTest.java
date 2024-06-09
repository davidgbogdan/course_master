import com.project.entity.*;
import com.project.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private Student student;

    @Mock
    private Course course;

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    void testCheckOverlappingSchedules_NoOverlappingSchedules() {
        //GIVEN
        Set<Schedule> courseSchedules = new HashSet<>();
        Set<Enrollment> studentEnrollments = new HashSet<>();
        Set<Course> courseSet =new HashSet<>();
        Teacher teacher =new Teacher("johnSmith","1234","John","Smith","075426453","john@Yahoo.com",courseSet);

        Course course2=new Course(1L,"english","test",23,15,studentEnrollments,courseSchedules,teacher);
        Schedule courseSchedule1=new Schedule(3L,"schedule3",LocalDate.now(),LocalDate.now(),DayOfWeek.MONDAY,LocalTime.of(12,30),LocalTime.of(14,30),course2);
        courseSchedules.add(courseSchedule1);

        Course course1=new Course(2L,"english","test",23,15,studentEnrollments,courseSchedules,teacher);
        Schedule courseSchedule3=new Schedule(3L,"schedule3",LocalDate.now(),LocalDate.now(),DayOfWeek.MONDAY,LocalTime.of(12,30),LocalTime.of(14,30),course1);
        courseSchedules.add(courseSchedule3);

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        studentEnrollments.add(enrollment);
        student.setEnrollments(studentEnrollments);

        //WHEN+THEN
        assertDoesNotThrow(() -> scheduleService.checkOverlappingSchedules(student, course));
    }

}
