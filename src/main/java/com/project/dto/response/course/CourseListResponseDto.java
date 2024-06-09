package com.project.dto.response.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseListResponseDto {
    List<CourseResponseDto> courses;
}
