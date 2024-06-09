package com.project.exception.enrollment;

import com.project.exception.CoursePlatformException;
import org.springframework.http.HttpStatus;

public class EnrollmentAlreadyExistsException extends CoursePlatformException {
    public EnrollmentAlreadyExistsException(){
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("ENROLLMENT_ALREADY_EXISTS");
    }
}
