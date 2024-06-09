package com.project.exception.course;

import com.project.exception.CoursePlatformException;
import org.springframework.http.HttpStatus;

public class CourseAlreadyExistsException extends CoursePlatformException {
    public CourseAlreadyExistsException(){
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("COURSE_ALREADY_EXISTS");
    }
}
