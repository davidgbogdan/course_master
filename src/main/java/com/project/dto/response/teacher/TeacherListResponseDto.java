package com.project.dto.response.teacher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherListResponseDto {
    List<TeacherResponseDto> teachers;
}
