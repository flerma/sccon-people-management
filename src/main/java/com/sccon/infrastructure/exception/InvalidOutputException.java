package com.sccon.infrastructure.exception;

public class InvalidOutputException extends RuntimeException {

    public InvalidOutputException(String message) {
        super(message);
    }

    public InvalidOutputException(String message, Throwable cause) {
        super(message, cause);
    }

}
