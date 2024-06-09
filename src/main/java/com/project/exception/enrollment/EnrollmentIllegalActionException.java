package com.project.exception.enrollment;

import com.project.exception.CoursePlatformException;
import org.springframework.http.HttpStatus;

public class EnrollmentIllegalActionException extends CoursePlatformException {
    public EnrollmentIllegalActionException(){
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("ILLEGAL_ENROLLMENT_ACTION");
    }
}
