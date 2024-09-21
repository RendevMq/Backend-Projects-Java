package com.example.presentation.dto;


import lombok.*;

import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToDoDTO {

    private String id;

    @NotBlank(message = "El título es obligatorio")
    private String title;

    private String description;

    private Boolean completed;  // Opcional en creación, requerido en actualización

    private String userId;  // ID del usuario propietario de la tarea, solo se usará en las respuestas
}
