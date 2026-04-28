package com.ensta.university.exception;

/**
 * Thrown when attempting to find or operate on a non-existent entity
 */
public class EntityNotFoundException extends UniversityException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
