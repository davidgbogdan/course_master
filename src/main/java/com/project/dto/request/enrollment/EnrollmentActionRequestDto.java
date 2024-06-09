package com.project.dto.request.enrollment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentActionRequestDto {
    @NotBlank
    String studentUsername;
    @NotBlank
    String action;
}
