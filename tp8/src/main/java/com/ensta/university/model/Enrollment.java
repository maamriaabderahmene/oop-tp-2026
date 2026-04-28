package com.ensta.university.model;

import java.util.Objects;

/**
 * Represents an enrollment relationship between a student and a course.
 * Uses the composite pattern to manage the many-to-many relationship.
 */
public class Enrollment {
    private final Student student;
    private final Course course;
    private final String enrollmentId;

    /**
     * Creates an enrollment record
     */
    Enrollment(Student student, Course course) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        if (student.isDeleted()) {
            throw new IllegalArgumentException("Cannot enroll a deleted student");
        }
        this.student = student;
        this.course = course;
        this.enrollmentId = student.getId() + "-" + course.getCode();
    }

    Student getStudent() {
        return student;
    }

    Course getCourse() {
        return course;
    }

    String getEnrollmentId() {
        return enrollmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment that = (Enrollment) o;
        return enrollmentId.equals(that.enrollmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enrollmentId);
    }

    @Override
    public String toString() {
        return String.format("Enrollment{student='%s', course='%s'}",
                student.getId(), course.getCode());
    }
}

