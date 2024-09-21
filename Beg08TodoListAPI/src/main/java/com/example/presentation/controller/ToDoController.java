package com.example.presentation.controller;

import com.example.presentation.dto.ToDoDTO;
import com.example.service.interfaces.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class ToDoController {

    @Autowired
    private ToDoService toDoService;

    // Crear una nueva tarea
    @PostMapping
    public ResponseEntity<ToDoDTO> createToDo(@Valid @RequestBody ToDoDTO toDoDTO) {
        ToDoDTO createdToDo = toDoService.createToDo(toDoDTO);  // El userId se maneja en el servicio
        return new ResponseEntity<>(createdToDo, HttpStatus.CREATED);
    }

    // Actualizar una tarea existente
    @PutMapping("/{id}")
    public ResponseEntity<ToDoDTO> updateToDo(@PathVariable String id, @Valid @RequestBody ToDoDTO toDoDTO) {
        ToDoDTO updatedToDo = toDoService.updateToDo(id, toDoDTO);  // El userId se maneja en el servicio
        return ResponseEntity.ok(updatedToDo);
    }

    // Eliminar una tarea
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToDo(@PathVariable String id) {
        toDoService.deleteToDo(id);  // El userId se maneja en el servicio
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Obtener todas las tareas con paginación y filtrado
    @GetMapping
    public ResponseEntity<List<ToDoDTO>> getAllToDosByUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Boolean completed) {

        Pageable pageable = PageRequest.of(page, limit);
        List<ToDoDTO> todos = toDoService.getAllToDosByUser(pageable, title, completed);  // El userId se maneja en el servicio

        return ResponseEntity.ok(todos);
    }

    // Obtener una tarea por ID
    @GetMapping("/{id}")
    public ResponseEntity<ToDoDTO> getToDoById(@PathVariable String id) {
        ToDoDTO toDoDTO = toDoService.getToDoById(id);  // El userId se maneja en el servicio
        return ResponseEntity.ok(toDoDTO);
    }
}
