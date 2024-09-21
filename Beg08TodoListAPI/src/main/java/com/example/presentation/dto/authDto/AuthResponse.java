package com.example.presentation.dto.authDto;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"email", "message", "token", "success"})
public record AuthResponse(
        String email,
        String message,
        String token,
        boolean success) {
}
