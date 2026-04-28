package com.ensta.university.exception;

/**
 * Base exception class for all university system exceptions
 */
public class UniversityException extends Exception {
    public UniversityException(String message) {
        super(message);
    }

    public UniversityException(String message, Throwable cause) {
        super(message, cause);
    }
}
