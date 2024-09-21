package com.example.presentation.dto.authDto;

import jakarta.validation.constraints.NotBlank;

public record AuthCreateUserRequest(
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String name) {
}
