package com.project.dto.response.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponseDto {
    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
}
