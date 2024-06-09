package com.project.dto.response.grade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeResponseDto {
    private Long id;
    private int finalGrade;
    private String courseName;
    private String courseId;
}
