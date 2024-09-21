package com.example.service.exception;

import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

public class ToDoNotFoundException extends RuntimeException {

    private final HttpStatus status;
    private final LocalDateTime timestamp;

    public ToDoNotFoundException(String message) {
        super(message);
        this.status = HttpStatus.NOT_FOUND;  // Código de estado HTTP 404
        this.timestamp = LocalDateTime.now();  // Momento en que ocurrió la excepción
    }

    public HttpStatus getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}


