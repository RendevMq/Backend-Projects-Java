package com.example.service.interfaces;


import com.example.presentation.dto.ExpenseDTO;

import java.util.List;
import java.util.Optional;

public interface IExpenseService {

    // Crear un nuevo gasto
    ExpenseDTO createExpense(ExpenseDTO expenseDTO);

    // Actualizar un gasto existente
    ExpenseDTO updateExpense(Long id, ExpenseDTO expenseDTO);

    // Eliminar un gasto
    void deleteExpense(Long id);

    // Obtener todos los gastos de un usuario
    List<ExpenseDTO> getExpensesByUserId(Long userId);

    // Obtener todos los gastos en una categoría específica
    List<ExpenseDTO> getExpensesByCategoryId(Long categoryId);

    // Obtener un gasto por ID
    Optional<ExpenseDTO> getExpenseById(Long id);

    List<ExpenseDTO> getExpenses(String username, String filter, String startDate, String endDate);
}
