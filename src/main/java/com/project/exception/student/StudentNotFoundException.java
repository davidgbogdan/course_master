package com.project.exception.student;

import com.project.exception.CoursePlatformException;
import org.springframework.http.HttpStatus;

public class StudentNotFoundException extends CoursePlatformException {
    public StudentNotFoundException(){
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setMessage("STUDENT_NOT_FOUND");
    }
}
