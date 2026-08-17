package com.assignment6.secureLibraryManagement.exception;

public class UserAlreadyBorrowedException extends RuntimeException {
    public UserAlreadyBorrowedException(String message, Long id) {
        super(String.format(message, id));
    }
}
