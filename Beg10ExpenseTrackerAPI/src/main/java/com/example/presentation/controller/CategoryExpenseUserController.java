package com.example.presentation.controller;


import com.example.presentation.dto.CategoryDTO;
import com.example.presentation.dto.ExpenseDTO;
import com.example.presentation.dto.UserDTO;

import com.example.service.interfaces.ICategoryService;
import com.example.service.interfaces.IExpenseService;
import com.example.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryExpenseUserController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IExpenseService expenseService;

    @Autowired
    private IUserService userService;

    // ========================== OPERACIONES SOBRE CATEGORÍAS ============================= //

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/categories")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Categoría eliminada con éxito.");
    }

    // ========================== OPERACIONES SOBRE GASTOS ============================= //

    // Crear gasto: Solo Usuarios
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/expenses")
    public ResponseEntity<ExpenseDTO> createExpense(@RequestBody ExpenseDTO expenseDTO) {
        return ResponseEntity.ok(expenseService.createExpense(expenseDTO));
    }

    // Actualizar gasto: Solo Usuarios pueden actualizar sus propios gastos
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/expenses/{id}")
    public ResponseEntity<ExpenseDTO> updateExpense(@PathVariable Long id, @RequestBody ExpenseDTO expenseDTO) {
        return ResponseEntity.ok(expenseService.updateExpense(id, expenseDTO));
    }

    // Eliminar gasto: Solo Usuarios pueden eliminar sus propios gastos
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok("Gasto eliminado con éxito.");
    }

    // Listar y Filtrar gastos: Solo Usuarios o Administradores
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/expenses")
    public ResponseEntity<List<ExpenseDTO>> getExpenses(
            @RequestParam(required = false) String filter, // Filtro: last_week, last_month, last_3_months
            @RequestParam(required = false) String startDate, // Fecha inicio para filtro personalizado
            @RequestParam(required = false) String endDate   // Fecha fin para filtro personalizado
    ) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ExpenseDTO> expenses = expenseService.getExpenses(currentUsername, filter, startDate, endDate);
        return ResponseEntity.ok(expenses);
    }
    // ========================== GESTIÓN DE USUARIOS (SOLO ADMIN) ============================= //

    // Crear usuario: Solo Admin
    /*@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }*/

    // Eliminar usuario: Solo Admin
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Usuario eliminado con éxito.");
    }

    // Obtener todos los usuarios: Solo Admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
