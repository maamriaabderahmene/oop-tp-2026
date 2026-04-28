package com.ensta.university.exception;

/**
 * Thrown when attempting to enroll a student in a course that is at capacity
 */
public class CourseFullException extends UniversityException {
    public CourseFullException(String message) {
        super(message);
    }
}
