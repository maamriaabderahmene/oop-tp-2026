package com.ensta.university.exception;

/**
 * Thrown when attempting to add a duplicate student or course
 */
public class DuplicateEntityException extends UniversityException {
    public DuplicateEntityException(String message) {
        super(message);
    }
}
