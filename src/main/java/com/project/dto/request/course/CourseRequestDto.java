package com.project.dto.request.course;

import com.project.dto.request.schedule.ScheduleRequestDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    @Min(1)
    private int maxAttendees;
    @NotNull
    @NotEmpty
    private List<ScheduleRequestDto> schedules;
}
