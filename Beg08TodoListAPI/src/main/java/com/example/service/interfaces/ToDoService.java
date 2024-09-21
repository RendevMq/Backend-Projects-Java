package com.example.service.interfaces;

import com.example.presentation.dto.ToDoDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ToDoService {

    // Crear una nueva tarea
    ToDoDTO createToDo(ToDoDTO toDoDTO);

    // Actualizar una tarea existente
    ToDoDTO updateToDo(String id, ToDoDTO toDoDTO);

    // Eliminar una tarea
    void deleteToDo(String id);

    // Obtener todas las tareas de un usuario
    //List<ToDoDTO> getAllToDosByUser(String userId);
    List<ToDoDTO> getAllToDosByUser(Pageable pageable, String title, Boolean completed);
    // Obtener una tarea por ID
    ToDoDTO getToDoById(String id);
}
