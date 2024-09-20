package com.example.service.implementation;

import com.example.persistence.entity.CategoryEntity;
import com.example.persistence.entity.authEntities.UserEntity;
import com.example.persistence.repository.CategoryRepository;
import com.example.persistence.repository.UserRepository;
import com.example.presentation.dto.CategoryDTO;
import com.example.presentation.dto.ExpenseDTO;
import com.example.service.exception.InvalidOperationException;
import com.example.service.exception.ResourceNotFoundException;
import com.example.service.interfaces.ICategoryService;
import com.example.service.interfaces.IExpenseService;
import com.example.service.interfaces.IUserService;
import com.example.util.EntityToDTOMapper;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private UserRepository userRepository;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        // Verificar si el usuario autenticado es un administrador
        boolean isAdmin = userService.isAdmin();

        // Configurar automáticamente el valor de isGlobal en función del rol del usuario
        if (isAdmin) {
            categoryDTO.setGlobal(true);  // Los administradores crean categorías globales
        } else {
            categoryDTO.setGlobal(false);  // Los usuarios normales crean categorías personalizadas
        }

        // Crear una nueva categoría
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(categoryDTO.getName());
        categoryEntity.setGlobal(categoryDTO.isGlobal());

        // Asignar el usuario si la categoría no es global
        if (!categoryDTO.isGlobal()) {
            // Obtener el usuario autenticado desde la base de datos
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity userEntity = userRepository.findUserEntityByUsername(currentUsername)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", "username", currentUsername));

            categoryEntity.setUser(userEntity);
        }

        // Guardar la categoría
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

    /*@Override
    public List<CategoryDTO> getAllCategories(boolean isGlobal, Long userId) {
        List<CategoryEntity> categories;
        if (isGlobal) {
            categories = categoryRepository.findByIsGlobalTrue();
        } else {
            categories = categoryRepository.findByIsGlobalFalseAndUser_Id(userId);
        }
        return categories.stream().map(EntityToDTOMapper::mapToCategoryDTO).collect(Collectors.toList());
    }*/

    @Override
    public Optional<CategoryDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id).map(EntityToDTOMapper::mapToCategoryDTO);
    }


    // Nuevo método para obtener las categorías de un usuario
    @Override
    public List<CategoryDTO> getUserCategories(String username) {
        // Obtener al usuario
        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "username", username));

        // Obtener categorías globales y personalizadas del usuario
        List<CategoryEntity> categories = categoryRepository.findByIsGlobalTrue();
        categories.addAll(categoryRepository.findByUser_Id(userEntity.getId()));

        return categories.stream()
                .map(EntityToDTOMapper::mapToCategoryDTO)
                .collect(Collectors.toList());
    }

    // Nuevo métodoo para que el ADMIN obtenga todas las categorías
    @Override
    public List<CategoryDTO> getAllCategories() {
        List<CategoryEntity> categories = categoryRepository.findAll();  // Obtener todas las categorías
        return categories.stream()
                .map(EntityToDTOMapper::mapToCategoryDTO)
                .collect(Collectors.toList());
    }


}
