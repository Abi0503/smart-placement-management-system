package com.placement.portal.exception;

/**
 * Thrown when business logic fails in an expected way,
 * e.g. "email already exists" or "invalid credentials".
 */
public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}