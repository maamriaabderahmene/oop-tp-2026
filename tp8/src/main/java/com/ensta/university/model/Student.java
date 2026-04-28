package com.ensta.university.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a student in the university system.
 * Implements soft delete pattern with isDeleted flag.
 */
public class Student {
    private final String id;
    private String name;
    private final Set<Course> enrolledCourses;
    private boolean isDeleted;


        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be null or empty");
        }
        this.id = id.trim();
        this.name = name.trim();
        this.enrolledCourses = new HashSet<>();
        this.isDeleted = false;
    }

    /**
     * Gets the student ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the student name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets an unmodifiable copy of enrolled courses
     */
    public Set<Course> getEnrolledCourses() {
        return new HashSet<>(enrolledCourses);
    }

    /**
     * Gets the number of courses the student is enrolled in
     */
    public int getEnrolledCourseCount() {
        return enrolledCourses.size();
    }

    /**
     * Checks if student is soft-deleted
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * Updates student name
     */

    }

    /**
     * Soft delete the student - marks as deleted but keeps in system
     */
    void softDelete() {
        this.isDeleted = true;
    }

    /**
     * Restore a soft-deleted student
     */
    void restore() {
        this.isDeleted = false;
    }

    /**
     * Add a course to student's enrollment
     */
    void addCourse(Course course) {
        enrolledCourses.add(course);
    }

    /**
     * Remove a course from student's enrollment
     */
    void removeCourse(Course course) {
        enrolledCourses.remove(course);
    }

    /**
     * Clear all courses from student (used during hard delete)
     */
    void clearCourses() {
        enrolledCourses.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id.equals(student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Student{id='%s', name='%s', enrolledCourses=%d, isDeleted=%b}",
                id, name, enrolledCourses.size(), isDeleted);
    }
}

