package com.project.exception.course;

import com.project.exception.CoursePlatformException;
import org.springframework.http.HttpStatus;

public class CourseNotFoundException extends CoursePlatformException {
    public CourseNotFoundException(){
        this.setHttpStatus(HttpStatus.NOT_FOUND);
        this.setMessage("COURSE_NOT_FOUND");
    }
}
