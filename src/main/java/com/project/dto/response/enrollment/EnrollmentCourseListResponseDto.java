package com.project.dto.response.enrollment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentCourseListResponseDto {
    List<EnrollmentCourseResponseDto> enrollments;
}
