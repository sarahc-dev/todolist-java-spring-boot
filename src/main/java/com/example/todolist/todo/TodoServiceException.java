package com.example.todolist.todo;

import org.springframework.http.HttpStatus;

public class TodoServiceException extends RuntimeException {
    private final HttpStatus httpStatus;

    public TodoServiceException(String message, Throwable cause) {
        this(message, cause, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public TodoServiceException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
