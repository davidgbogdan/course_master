package com.project.dto.response.security;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSessionDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
