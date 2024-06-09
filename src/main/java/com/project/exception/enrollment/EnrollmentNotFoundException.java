package com.project.exception.enrollment;

import com.project.exception.CoursePlatformException;
import org.springframework.http.HttpStatus;

public class EnrollmentNotFoundException extends CoursePlatformException {
    public EnrollmentNotFoundException(){
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setMessage("ENROLLMENT_REQUEST_NOT_FOUND");
    }
}
