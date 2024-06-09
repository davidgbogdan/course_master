package com.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, name = "max_attendees")
    private int maxAttendees;

    @Column(nullable = false, name = "current_attendees")
    @Builder.Default
    private int currentAttendees = 0;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(
            cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            fetch = FetchType.EAGER,
            mappedBy = "course",
            orphanRemoval = true
    )
    private Set<Enrollment> enrollments = new HashSet<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(
            cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            fetch = FetchType.EAGER,
            mappedBy = "course",
            orphanRemoval = true
    )
    private Set<Schedule> schedules = new HashSet<>();

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Teacher teacher;

    public void addSchedule(Schedule schedule){
        schedules.add(schedule);
        schedule.setCourse(this);
    }

    public void addEnrollmentToCourse(Enrollment enrollment, Student student){
        enrollments.add(enrollment);
        enrollment.setCourse(this);

        student.addEnrollmentToStudent(enrollment);
        enrollment.setStudent(student);
    }

    public void incrementCurrentAttendees(){
        currentAttendees++;
    }

    public void decrementCurrentAttendees(){
        currentAttendees--;
    }

    public boolean checkIfFull() {
        return maxAttendees == currentAttendees;
    }
}
