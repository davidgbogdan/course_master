package com.project.dto.response.course;

import com.project.dto.response.schedule.ScheduleResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponseDto {
    private Long id;
    private String name;
    private String description;
    private int maxAttendees;
    private int currentAttendees;
    private List<ScheduleResponseDto> schedules;
    private String teacherUsername;
}
