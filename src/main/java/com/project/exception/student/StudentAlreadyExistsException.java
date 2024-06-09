package com.project.exception.student;

import com.project.exception.CoursePlatformException;
import org.springframework.http.HttpStatus;

public class StudentAlreadyExistsException extends CoursePlatformException {
    public StudentAlreadyExistsException(){
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("STUDENT_ALREADY_EXISTS");
    }
}
