package com.project.exception.teacher;

import com.project.exception.CoursePlatformException;
import org.springframework.http.HttpStatus;

public class TeacherAlreadyExistsException extends CoursePlatformException {
    public TeacherAlreadyExistsException(){
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("TEACHER_ALREADY_EXISTS");
    }
}
