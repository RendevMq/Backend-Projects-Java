package com.example.persistence.repository;

import com.example.persistence.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    // Encontrar todos los gastos asociados a un usuario
    List<ExpenseEntity> findByUser_Id(Long userId);

    // Encontrar todos los gastos asociados a una categoría
    List<ExpenseEntity> findByCategory_Id(Long categoryId);

    // Encontrar todos los gastos de un usuario en una categoría específica
    List<ExpenseEntity> findByUser_IdAndCategory_Id(Long userId, Long categoryId);
}
