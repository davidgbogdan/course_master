package com.project.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class CoursePlatformException extends RuntimeException {
    private HttpStatus httpStatus;
    private String message;
}