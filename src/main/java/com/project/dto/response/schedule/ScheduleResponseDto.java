package com.project.dto.response.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponseDto {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private DayOfWeek weekDay;
    private LocalTime startTime;
    private LocalTime endTime;
}
