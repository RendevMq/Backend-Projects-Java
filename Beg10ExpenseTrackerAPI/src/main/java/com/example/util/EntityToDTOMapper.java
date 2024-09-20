package com.example.util;

import com.example.persistence.entity.CategoryEntity;
import com.example.persistence.entity.ExpenseEntity;
import com.example.persistence.entity.authEntities.UserEntity;
import com.example.presentation.dto.CategoryDTO;
import com.example.presentation.dto.ExpenseDTO;
import com.example.presentation.dto.UserDTO;

public class EntityToDTOMapper {

    // Métodoo para mapear CategoryEntity a CategoryDTO
    public static CategoryDTO mapToCategoryDTO(CategoryEntity categoryEntity) {
        return CategoryDTO.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .isGlobal(categoryEntity.isGlobal())
                .userId(categoryEntity.getUser() != null ? categoryEntity.getUser().getId() : null)
                .build();
    }

    // Métodoo para mapear ExpenseEntity a ExpenseDTO
    public static ExpenseDTO mapToExpenseDTO(ExpenseEntity expenseEntity) {
        return ExpenseDTO.builder()
                .id(expenseEntity.getId())
                .amount(expenseEntity.getAmount())
                .description(expenseEntity.getDescription())
                .date(expenseEntity.getDate())
                .userId(expenseEntity.getUser().getId())
                .categoryId(expenseEntity.getCategory().getId())
                .build();
    }

    // Métodoo para mapear UserEntity a UserDTO
    public static UserDTO mapToUserDTO(UserEntity userEntity) {
        return UserDTO.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .isEnable(userEntity.isEnable())
                .accountNoExpired(userEntity.isAccountNoExpired())
                .accountNoLocked(userEntity.isAccountNoLocked())
                .credentialNoExpired(userEntity.isCredentialNoExpired())
                .build();
    }
}
