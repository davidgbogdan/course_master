package com.project.exception.course;

import com.project.exception.CoursePlatformException;
import org.springframework.http.HttpStatus;

public class CourseFullException extends CoursePlatformException {
    public CourseFullException(){
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("COURSE_MAX_CAPACITY");
    }
}
