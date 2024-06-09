package com.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name = "start_date")
    private LocalDate startDate;

    @Column(nullable = false, name = "end_date")
    private LocalDate endDate;

    @Column(nullable = false, name = "week_day")
    private DayOfWeek weekDay;

    @Column(nullable = false, name = "start_time")
    private LocalTime startTime;

    @Column(nullable = false, name = "end_time")
    private LocalTime endTime;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Course course;

    public void setCourse(Course course) {
        this.course = course;
    }
}
