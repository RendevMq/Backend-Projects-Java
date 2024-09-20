package com.example.persistence.repository;

import com.example.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    // Encontrar una categoría por su nombre
    Optional<CategoryEntity> findByName(String name);

    // Encontrar todas las categorías globales
    List<CategoryEntity> findByIsGlobalTrue();

    // Encontrar todas las categorías personalizadas por usuario
    List<CategoryEntity> findByIsGlobalFalseAndUser_Id(Long userId);

    // Obtener las categorías personalizadas de un usuario
    List<CategoryEntity> findByUser_Id(Long userId);

    // Opcionalmente, un método para obtener todas las categorías
    List<CategoryEntity> findAll();

}
