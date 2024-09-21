package com.example.presentation.advice;


import com.example.service.exception.ToDoNotFoundException;
import com.example.service.exception.UnauthorizedOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Manejo de ToDoNotFoundException
    @ExceptionHandler(ToDoNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleToDoNotFoundException(ToDoNotFoundException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("timestamp", ex.getTimestamp());
        errorDetails.put("status", ex.getStatus().value());

        return new ResponseEntity<>(errorDetails, ex.getStatus());
    }

    // Manejo de UnauthorizedOperationException
    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedOperationException(UnauthorizedOperationException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("timestamp", ex.getTimestamp());
        errorDetails.put("status", ex.getStatus().value());

        return new ResponseEntity<>(errorDetails, ex.getStatus());
    }

    // Manejo de excepciones generales
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
