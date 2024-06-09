package com.project.exception.teacher;

import com.project.exception.CoursePlatformException;
import org.springframework.http.HttpStatus;

public class TeacherNotFoundException extends CoursePlatformException {
    public TeacherNotFoundException(){
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setMessage("TEACHER_NOT_FOUND");
    }
}
