package com.project.exception.teacher;

import com.project.exception.CoursePlatformException;
import org.springframework.http.HttpStatus;

public class TeacherInvalidActionException extends CoursePlatformException {
    public TeacherInvalidActionException(){
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("TEACHER_INVALID_ACTION");
    }
}
