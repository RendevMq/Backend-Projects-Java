package com.example.persistence.repository;



import com.example.persistence.entity.ToDoItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoRepository extends MongoRepository<ToDoItem, String> {

    // Buscar todas las tareas de un usuario con paginación
    Page<ToDoItem> findByUserId(String userId, Pageable pageable);

    // Buscar tareas por título y usuario con paginación
    Page<ToDoItem> findByUserIdAndTitleContaining(String userId, String title, Pageable pageable);

    // Buscar tareas por estado de completado y usuario con paginación
    Page<ToDoItem> findByUserIdAndCompleted(String userId, Boolean completed, Pageable pageable);

    // Filtrar por título y estado (completado) junto con el usuario
    Page<ToDoItem> findByUserIdAndTitleContainingAndCompleted(String userId, String title, Boolean completed, Pageable pageable);
}
