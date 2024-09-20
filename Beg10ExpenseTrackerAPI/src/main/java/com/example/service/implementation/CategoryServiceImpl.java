package com.example.service.implementation;

import com.example.persistence.entity.CategoryEntity;
import com.example.persistence.entity.authEntities.UserEntity;
import com.example.persistence.repository.CategoryRepository;
import com.example.presentation.dto.CategoryDTO;
import com.example.presentation.dto.ExpenseDTO;
import com.example.service.exception.InvalidOperationException;
import com.example.service.exception.ResourceNotFoundException;
import com.example.service.interfaces.ICategoryService;
import com.example.service.interfaces.IExpenseService;
import com.example.service.interfaces.IUserService;
import com.example.util.EntityToDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IExpenseService expenseService; // Corregido: Ahora inyecta correctamente IExpenseService

    @Autowired
    private IUserService userService;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryDTO.isGlobal() && !userService.isAdmin()) {
            throw new InvalidOperationException("Solo los administradores pueden crear categorías globales.");
        }

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(categoryDTO.getName());
        categoryEntity.setGlobal(categoryDTO.isGlobal());

        // Asignar usuario si la categoría no es global
        if (!categoryDTO.isGlobal()) {
            categoryEntity.setUser(UserEntity.builder().id(categoryDTO.getUserId()).build());
        }

        CategoryEntity savedCategory = categoryRepository.save(categoryEntity);
        return EntityToDTOMapper.mapToCategoryDTO(savedCategory);
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", id));

        if (categoryEntity.isGlobal() && !userService.isAdmin()) {
            throw new InvalidOperationException("Solo los administradores pueden actualizar categorías globales.");
        }
        if (!categoryEntity.isGlobal() && !userService.isOwner(categoryEntity.getUser().getId())) {
            throw new InvalidOperationException("No puedes actualizar una categoría que no te pertenece.");
        }

        categoryEntity.setName(categoryDTO.getName());
        CategoryEntity updatedCategory = categoryRepository.save(categoryEntity);
        return EntityToDTOMapper.mapToCategoryDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", id));

        if (categoryEntity.isGlobal() && !userService.isAdmin()) {
            throw new InvalidOperationException("Solo los administradores pueden eliminar categorías globales.");
        }
        if (!categoryEntity.isGlobal() && !userService.isOwner(categoryEntity.getUser().getId())) {
            throw new InvalidOperationException("No puedes eliminar una categoría que no te pertenece.");
        }

        // Verificar si hay gastos asociados a la categoría
        List<ExpenseDTO> associatedExpenses = expenseService.getExpensesByCategoryId(id);
        if (!associatedExpenses.isEmpty()) {
            throw new InvalidOperationException(
                    String.format("No se puede eliminar la categoría '%s' porque hay gastos asociados.", categoryEntity.getName()));
        }

        categoryRepository.delete(categoryEntity);
    }

    @Override
    public List<CategoryDTO> getAllCategories(boolean isGlobal, Long userId) {
        List<CategoryEntity> categories;
        if (isGlobal) {
            categories = categoryRepository.findByIsGlobalTrue();
        } else {
            categories = categoryRepository.findByIsGlobalFalseAndUser_Id(userId);
        }
        return categories.stream().map(EntityToDTOMapper::mapToCategoryDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<CategoryDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id).map(EntityToDTOMapper::mapToCategoryDTO);
    }

}
