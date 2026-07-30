package com.assignment6.secureLibraryManagement.exception;

public class UnauthorizedRequestException extends RuntimeException {
    public UnauthorizedRequestException(String message) {
        super(message);
    }
}
