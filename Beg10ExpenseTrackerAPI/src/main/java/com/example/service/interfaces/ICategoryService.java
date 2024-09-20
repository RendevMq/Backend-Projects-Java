package com.example.service.interfaces;

import com.example.presentation.dto.CategoryDTO;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {

    // Crear una nueva categoría
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    // Actualizar una categoría existente
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    // Eliminar una categoría
    void deleteCategory(Long id);

    // Obtener todas las categorías (globales o personalizadas para un usuario)
    List<CategoryDTO> getAllCategories(boolean isGlobal, Long userId);

    // Obtener una categoría por ID
    Optional<CategoryDTO> getCategoryById(Long id);
}
