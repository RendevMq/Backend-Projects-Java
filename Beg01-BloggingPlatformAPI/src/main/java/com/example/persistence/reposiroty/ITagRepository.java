package com.example.persistence.reposiroty;

import com.example.persistence.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ITagRepository  extends JpaRepository<TagEntity, Long> {
    // Métodoo para encontrar etiqueta por nombre
    Optional<TagEntity> findByName(String name);
}
