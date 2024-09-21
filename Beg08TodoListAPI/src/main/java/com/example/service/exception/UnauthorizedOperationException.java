package com.example.service.exception;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

public class UnauthorizedOperationException extends RuntimeException {

    private final HttpStatus status;
    private final LocalDateTime timestamp;

    public UnauthorizedOperationException(String message) {
        super(message);
        this.status = HttpStatus.FORBIDDEN;  // Código de estado HTTP 403
        this.timestamp = LocalDateTime.now();  // Momento en que ocurrió la excepción
    }

    public HttpStatus getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
