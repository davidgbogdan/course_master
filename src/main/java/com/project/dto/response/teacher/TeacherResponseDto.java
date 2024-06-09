package com.project.dto.response.teacher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherResponseDto {
    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
}
