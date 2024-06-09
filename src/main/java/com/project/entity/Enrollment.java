package com.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.PENDING;

    @Column(nullable = false, name = "final_grade")
    private int finalGrade;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Student student;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Course course;

    public void approveEnrollment() {
        this.setStatus(EnrollmentStatus.APPROVED);
    }

    public void denyEnrollment() {
        this.setStatus(EnrollmentStatus.DENIED);
    }
}
