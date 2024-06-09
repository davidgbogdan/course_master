package com.project.dto.response.enrollment;

import com.project.entity.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentStudentResponseDto {
    private Long id;
    private EnrollmentStatus status;
    private int finalGrade;
    private String studentFirstName;
    private String studentLastName;
    private String studentUsername;
}
