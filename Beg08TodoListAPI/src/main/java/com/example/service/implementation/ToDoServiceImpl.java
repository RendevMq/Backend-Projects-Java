package com.example.service.implementation;

import com.example.persistence.entity.ToDoItem;
import com.example.persistence.entity.authEntities.UserEntity;
import com.example.persistence.repository.ToDoRepository;
import com.example.persistence.repository.UserRepository;
import com.example.presentation.dto.ToDoDTO;
import com.example.service.exception.ToDoNotFoundException;
import com.example.service.exception.UnauthorizedOperationException;
import com.example.service.interfaces.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToDoServiceImpl implements ToDoService {

    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private UserRepository userRepository;  // Para obtener los datos del usuario autenticado

    // Métodoo auxiliar para obtener el usuario autenticado desde el contexto de seguridad
    private UserEntity getAuthenticatedUser() {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new UnauthorizedOperationException("Usuario no encontrado"));
    }

    // Crear una nueva tarea
    @Override
    public ToDoDTO createToDo(ToDoDTO toDoDTO) {
        UserEntity userEntity = getAuthenticatedUser();  // Obtener el usuario autenticado

        // Crear la tarea asignándola al usuario autenticado
        ToDoItem toDoItem = ToDoItem.builder()
                .title(toDoDTO.getTitle())
                .description(toDoDTO.getDescription())
                .completed(false)  // Por defecto, la tarea está incompleta
                .userId(userEntity.getId())  // Asignar el ID del usuario autenticado
                .build();

        ToDoItem savedItem = toDoRepository.save(toDoItem);
        return mapToDTO(savedItem);
    }

    // Actualizar una tarea existente
    @Override
    public ToDoDTO updateToDo(String id, ToDoDTO toDoDTO) {
        ToDoItem toDoItem = toDoRepository.findById(id)
                .orElseThrow(() -> new ToDoNotFoundException("La tarea con id " + id + " no existe"));

        UserEntity userEntity = getAuthenticatedUser();  // Obtener el usuario autenticado

        // Verificar si el usuario autenticado es el propietario de la tarea
        if (!toDoItem.getUserId().equals(userEntity.getId())) {
            throw new UnauthorizedOperationException("No tienes permisos para actualizar esta tarea.");
        }

        // Actualizar los campos de la tarea
        toDoItem.setTitle(toDoDTO.getTitle());
        toDoItem.setDescription(toDoDTO.getDescription());
        toDoItem.setCompleted(toDoDTO.getCompleted());

        ToDoItem updatedItem = toDoRepository.save(toDoItem);
        return mapToDTO(updatedItem);
    }

    // Eliminar una tarea
    @Override
    public void deleteToDo(String id) {
        ToDoItem toDoItem = toDoRepository.findById(id)
                .orElseThrow(() -> new ToDoNotFoundException("La tarea con id " + id + " no existe"));

        UserEntity userEntity = getAuthenticatedUser();  // Obtener el usuario autenticado

        // Verificar si el usuario autenticado es el propietario de la tarea
        if (!toDoItem.getUserId().equals(userEntity.getId())) {
            throw new UnauthorizedOperationException("No tienes permisos para eliminar esta tarea.");
        }

        toDoRepository.deleteById(id);
    }

    // Obtener todas las tareas del usuario autenticado
    @Override
    public List<ToDoDTO> getAllToDosByUser(Pageable pageable, String title, Boolean completed) {
        UserEntity userEntity = getAuthenticatedUser();  // Obtener el usuario autenticado

        // Filtrar tareas por título y estado de completado (si es aplicable)
        Page<ToDoItem> toDoPage;
        if (title != null && completed != null) {
            toDoPage = toDoRepository.findByUserIdAndTitleContainingAndCompleted(userEntity.getId(), title, completed, pageable);
        } else if (title != null) {
            toDoPage = toDoRepository.findByUserIdAndTitleContaining(userEntity.getId(), title, pageable);
        } else if (completed != null) {
            toDoPage = toDoRepository.findByUserIdAndCompleted(userEntity.getId(), completed, pageable);
        } else {
            toDoPage = toDoRepository.findByUserId(userEntity.getId(), pageable);
        }

        return toDoPage.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Obtener una tarea por ID
    @Override
    public ToDoDTO getToDoById(String id) {
        ToDoItem toDoItem = toDoRepository.findById(id)
                .orElseThrow(() -> new ToDoNotFoundException("La tarea con id " + id + " no existe"));

        UserEntity userEntity = getAuthenticatedUser();  // Obtener el usuario autenticado

        // Verificar si el usuario autenticado es el propietario de la tarea
        if (!toDoItem.getUserId().equals(userEntity.getId())) {
            throw new UnauthorizedOperationException("No tienes permisos para ver esta tarea.");
        }

        return mapToDTO(toDoItem);
    }

    // Método auxiliar para mapear una entidad ToDoItem a un DTO
    private ToDoDTO mapToDTO(ToDoItem toDoItem) {
        ToDoDTO dto = new ToDoDTO();
        dto.setId(toDoItem.getId());
        dto.setTitle(toDoItem.getTitle());
        dto.setDescription(toDoItem.getDescription());
        dto.setCompleted(toDoItem.isCompleted());
        dto.setUserId(toDoItem.getUserId());
        return dto;
    }
}
