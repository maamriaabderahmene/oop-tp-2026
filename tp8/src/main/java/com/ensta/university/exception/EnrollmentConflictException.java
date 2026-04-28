package com.ensta.university.exception;

/**
 * Thrown when there is a conflict in enrollment (e.g., duplicate enrollment)
 */
public class EnrollmentConflictException extends UniversityException {
    public EnrollmentConflictException(String message) {
        super(message);
    }
}
