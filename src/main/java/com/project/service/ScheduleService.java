package com.project.service;

import com.project.entity.Course;
import com.project.entity.Schedule;
import com.project.entity.Student;
import com.project.exception.schedule.ScheduleOverlappingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@AllArgsConstructor
public class ScheduleService {
    public void checkOverlappingSchedules(Student student, Course course) {

        var courseSchedules = course.getSchedules();

        var studentSchedules = student.getEnrollments().stream()
                .flatMap(enrollment -> enrollment.getCourse().getSchedules().stream())
                .toList();

        courseSchedules.forEach(courseSchedule ->
                studentSchedules.forEach(studentSchedule -> {
                    if (isScheduleOverlapping(courseSchedule, studentSchedule)) {
                        throw new ScheduleOverlappingException();
                    }
                })
        );
    }

    private boolean isScheduleOverlapping(Schedule schedule1, Schedule schedule2) {
        return isDateOverlapping(schedule1.getStartDate(), schedule1.getEndDate(), schedule2.getStartDate(), schedule2.getEndDate())
                && isDayOverlapping(schedule1.getWeekDay(), schedule2.getWeekDay())
                && isTimeOverlapping(schedule1.getStartTime(), schedule1.getEndTime(), schedule2.getStartTime(), schedule2.getEndTime());
    }

    private boolean isTimeOverlapping(LocalTime startTime1, LocalTime endTime1, LocalTime startTime2, LocalTime endTime2) {
        return startTime1.isBefore(endTime2) && endTime1.isAfter(startTime2);
    }

    private boolean isDayOverlapping(DayOfWeek weekDay1, DayOfWeek weekDay2) {
        return weekDay1.equals(weekDay2);
    }

    private boolean isDateOverlapping(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2, LocalDate endDate2) {
        return startDate1.isBefore(endDate2) && endDate1.isAfter(startDate2);
    }
}
