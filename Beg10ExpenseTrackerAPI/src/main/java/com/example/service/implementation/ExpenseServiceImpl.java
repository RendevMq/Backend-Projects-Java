package com.example.service.implementation;

import com.example.persistence.entity.CategoryEntity;
import com.example.persistence.entity.ExpenseEntity;
import com.example.persistence.entity.authEntities.UserEntity;
import com.example.persistence.repository.ExpenseRepository;
import com.example.presentation.dto.ExpenseDTO;
import com.example.service.exception.InvalidOperationException;
import com.example.service.exception.ResourceNotFoundException;
import com.example.service.interfaces.IExpenseService;
import com.example.service.interfaces.IUserService;
import com.example.util.EntityToDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements IExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private IUserService userService; // Inyectamos el servicio de usuario para verificar el propietario

    @Override
    public ExpenseDTO createExpense(ExpenseDTO expenseDTO) {
        ExpenseEntity expenseEntity = ExpenseEntity.builder()
                .amount(expenseDTO.getAmount())
                .description(expenseDTO.getDescription())
                .date(expenseDTO.getDate())
                .user(UserEntity.builder().id(expenseDTO.getUserId()).build()) // Usamos el builder para UserEntity con el ID
                .category(CategoryEntity.builder().id(expenseDTO.getCategoryId()).build()) // Usamos el builder para CategoryEntity con el ID
                .build();

        ExpenseEntity savedExpense = expenseRepository.save(expenseEntity);
        return EntityToDTOMapper.mapToExpenseDTO(savedExpense);
    }

    @Override
    public ExpenseDTO updateExpense(Long id, ExpenseDTO expenseDTO) {
        ExpenseEntity expenseEntity = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto", "id", id));

        // Verificamos que el usuario actual sea el propietario del gasto
        if (!userService.isOwner(expenseEntity.getUser().getId())) {
            throw new InvalidOperationException("No puedes actualizar un gasto que no te pertenece.");
        }

        // Actualizar detalles del gasto
        expenseEntity.setAmount(expenseDTO.getAmount());
        expenseEntity.setDescription(expenseDTO.getDescription());
        expenseEntity.setDate(expenseDTO.getDate());

        ExpenseEntity updatedExpense = expenseRepository.save(expenseEntity);
        return EntityToDTOMapper.mapToExpenseDTO(updatedExpense);
    }

    @Override
    public void deleteExpense(Long id) {
        ExpenseEntity expenseEntity = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto", "id", id));

        // Verificamos que el usuario actual sea el propietario del gasto
        if (!userService.isOwner(expenseEntity.getUser().getId())) {
            throw new InvalidOperationException("No puedes eliminar un gasto que no te pertenece.");
        }

        expenseRepository.delete(expenseEntity);
    }

    @Override
    public List<ExpenseDTO> getExpensesByUserId(Long userId) {
        List<ExpenseEntity> expenses = expenseRepository.findByUser_Id(userId);
        return expenses.stream().map(EntityToDTOMapper::mapToExpenseDTO).collect(Collectors.toList());
    }

    @Override
    public List<ExpenseDTO> getExpensesByCategoryId(Long categoryId) {
        List<ExpenseEntity> expenses = expenseRepository.findByCategory_Id(categoryId);
        return expenses.stream().map(EntityToDTOMapper::mapToExpenseDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<ExpenseDTO> getExpenseById(Long id) {
        return expenseRepository.findById(id).map(EntityToDTOMapper::mapToExpenseDTO);
    }
}
