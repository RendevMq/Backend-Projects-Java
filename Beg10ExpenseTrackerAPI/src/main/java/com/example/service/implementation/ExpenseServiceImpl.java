package com.example.service.implementation;

import com.example.persistence.entity.CategoryEntity;
import com.example.persistence.entity.ExpenseEntity;
import com.example.persistence.entity.authEntities.UserEntity;
import com.example.persistence.repository.ExpenseRepository;
import com.example.persistence.repository.UserRepository;
import com.example.presentation.dto.ExpenseDTO;
import com.example.presentation.dto.UserDTO;
import com.example.service.exception.InvalidOperationException;
import com.example.service.exception.ResourceNotFoundException;
import com.example.service.interfaces.IExpenseService;
import com.example.service.interfaces.IUserService;
import com.example.util.EntityToDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements IExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IUserService userService; // Inyectamos el servicio de usuario para verificar el propietario

    @Override
    public ExpenseDTO createExpense(ExpenseDTO expenseDTO) {
        // Obtener el nombre de usuario autenticado desde el contexto de seguridad
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // Obtener el usuario desde el repositorio
        UserEntity userEntity = userRepository.findUserEntityByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "username", currentUsername));

        // Crear el gasto asociando al usuario autenticado
        ExpenseEntity expenseEntity = ExpenseEntity.builder()
                .amount(expenseDTO.getAmount())
                .description(expenseDTO.getDescription())
                .date(expenseDTO.getDate())
                .user(userEntity)  // Asignar el usuario autenticado
                .category(CategoryEntity.builder().id(expenseDTO.getCategoryId()).build())  // Asignar la categoría
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

    @Override
    public List<ExpenseDTO> getExpenses(String username, String filter, String startDate, String endDate) {
        // Si es Admin, retorna todos los gastos
        if (userService.isAdmin()) {
            return getAllExpenses(filter, startDate, endDate);
        } else {
            // Si es un usuario regular, obtener solo sus gastos
            Optional<UserDTO> currentUser = userService.getUserByUsername(username);
            if (currentUser.isPresent()) {
                Long userId = currentUser.get().getId();
                return getExpensesByFilters(userId, filter, startDate, endDate);
            } else {
                throw new IllegalArgumentException("Usuario no encontrado");
            }
        }
    }

    // Métodoo para obtener todos los gastos si es ADMIN
    private List<ExpenseDTO> getAllExpenses(String filter, String startDate, String endDate) {
        LocalDate start;
        LocalDate end = LocalDate.now(); // Fecha de fin será hoy si no se especifica otra

        // Aplicar los filtros (igual que con los gastos individuales)
        if ("last_week".equals(filter)) {
            start = LocalDate.now().minusWeeks(1);
        } else if ("last_month".equals(filter)) {
            start = LocalDate.now().minusMonths(1);
        } else if ("last_3_months".equals(filter)) {
            start = LocalDate.now().minusMonths(3);
        } else if (startDate != null && endDate != null) {
            start = LocalDate.parse(startDate);
            end = LocalDate.parse(endDate);
        } else {
            // Sin filtros, retornar todos los gastos
            return expenseRepository.findAll().stream()
                    .map(EntityToDTOMapper::mapToExpenseDTO)
                    .collect(Collectors.toList());
        }

        // Retornar los gastos filtrados por rango de fechas (de todos los usuarios)
        return expenseRepository.findByDateBetween(start, end).stream()
                .map(EntityToDTOMapper::mapToExpenseDTO)
                .collect(Collectors.toList());
    }

    // Método para obtener los gastos del usuario autenticado
    private List<ExpenseDTO> getExpensesByFilters(Long userId, String filter, String startDate, String endDate) {
        LocalDate start;
        LocalDate end = LocalDate.now();

        if ("last_week".equals(filter)) {
            start = LocalDate.now().minusWeeks(1);
        } else if ("last_month".equals(filter)) {
            start = LocalDate.now().minusMonths(1);
        } else if ("last_3_months".equals(filter)) {
            start = LocalDate.now().minusMonths(3);
        } else if (startDate != null && endDate != null) {
            start = LocalDate.parse(startDate);
            end = LocalDate.parse(endDate);
        } else {
            // Sin filtros, retornar todos los gastos del usuario
            return expenseRepository.findByUser_Id(userId).stream()
                    .map(EntityToDTOMapper::mapToExpenseDTO)
                    .collect(Collectors.toList());
        }

        // Retornar los gastos filtrados por rango de fechas para el usuario
        return expenseRepository.findByUser_IdAndDateBetween(userId, start, end).stream()
                .map(EntityToDTOMapper::mapToExpenseDTO)
                .collect(Collectors.toList());
    }
}
