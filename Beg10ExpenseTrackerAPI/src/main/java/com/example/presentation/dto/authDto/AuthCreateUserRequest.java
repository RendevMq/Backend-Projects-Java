package com.example.presentation.dto.authDto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record AuthCreateUserRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String email,  // Añadir email
        @Valid AuthCreateRoleRequest roleRequest) { // Asegúrate de que sea necesario para tu lógica
}
