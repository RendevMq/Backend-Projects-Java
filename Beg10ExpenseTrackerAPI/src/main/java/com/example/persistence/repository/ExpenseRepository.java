package com.example.persistence.repository;

import com.example.persistence.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    // Encontrar todos los gastos asociados a un usuario
    List<ExpenseEntity> findByUser_Id(Long userId);

    // Encontrar todos los gastos asociados a una categoría
    List<ExpenseEntity> findByCategory_Id(Long categoryId);

    // Encontrar todos los gastos de un usuario en una categoría específica
    List<ExpenseEntity> findByUser_IdAndCategory_Id(Long userId, Long categoryId);

    // Listar los gastos de un usuario filtrados por rango de fechas
    List<ExpenseEntity> findByUser_IdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    // Listar todos los gastos filtrados por rango de fechas
    List<ExpenseEntity> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
