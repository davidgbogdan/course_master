package com.project.dto.request.grade;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeRequestDto {
    @NotBlank
    private String studentUsername;
    @NotNull
    @Min(1)
    @Max(10)
    private int finalGrade;
}
