package com.example.persistence.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "todos")  // Usamos MongoDB
public class ToDoItem {

    @Id
    private String id;  // ID generado por MongoDB

    private String title;  // Título de la tarea

    private String description;  // Descripción de la tarea

    private String userId;  // ID del usuario propietario de la tarea

    private boolean completed;  // Estado de la tarea (completada o no)
}
