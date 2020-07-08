package com.example.moviebookingdemo.coreapi.exception;

public class InvalidOperationException extends Exception {

    public InvalidOperationException(String message) {
        super(message);
    }

    public InvalidOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
