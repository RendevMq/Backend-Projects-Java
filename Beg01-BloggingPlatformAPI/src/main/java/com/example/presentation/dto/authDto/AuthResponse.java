package com.example.presentation.dto.authDto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username","message","jwt" }) //le damos un oden a la respuesta, le decimos  ajackson
public record AuthResponse(String username, String message, String jwt, boolean status) {
}
