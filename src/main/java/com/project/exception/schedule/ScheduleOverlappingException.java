package com.project.exception.schedule;

import com.project.exception.CoursePlatformException;
import org.springframework.http.HttpStatus;

public class ScheduleOverlappingException extends CoursePlatformException {
    public ScheduleOverlappingException(){
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
        this.setMessage("SCHEDULES_OVERLAP");
    }
}
