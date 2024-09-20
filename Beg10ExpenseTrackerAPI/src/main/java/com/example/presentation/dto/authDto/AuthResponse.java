package com.example.presentation.dto.authDto;



import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "message", "accessToken"})
public record AuthResponse(String username, String message, String accessToken, boolean status) {
}
