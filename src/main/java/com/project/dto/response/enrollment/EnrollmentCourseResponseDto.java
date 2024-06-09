package com.project.dto.response.enrollment;

import com.project.entity.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentCourseResponseDto {
    private Long id;
    private EnrollmentStatus status;
    private int finalGrade;
    private String courseName;
    private String courseId;
}
