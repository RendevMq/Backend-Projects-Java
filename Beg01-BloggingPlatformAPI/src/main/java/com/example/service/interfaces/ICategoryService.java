package com.example.service.interfaces;

import com.example.presentation.dto.CategoryDTO;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {

    // Crear o guardar una nueva categoría
    CategoryDTO save(CategoryDTO categoryDTO);

    // Buscar todas las categorías
    List<CategoryDTO> findAll();

    // Buscar una categoría por su ID
    Optional<CategoryDTO> findById(Long id);

    // Buscar una categoría por su nombre
    Optional<CategoryDTO> findByName(String name);

    // Eliminar una categoría por su ID
    void deleteById(Long id);
}
