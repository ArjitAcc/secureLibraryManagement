package com.assignment6.secureLibraryManagement.exception;

public class AlreadyBorrowedException extends RuntimeException {
    public AlreadyBorrowedException(String message) {
        super(message);
    }
}
