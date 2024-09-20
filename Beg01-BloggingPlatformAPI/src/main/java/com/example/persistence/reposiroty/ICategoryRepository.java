package com.example.persistence.reposiroty;

import com.example.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {

    // Métodoo para encontrar categoría por nombre
    Optional<CategoryEntity> findByName(String name);
}
