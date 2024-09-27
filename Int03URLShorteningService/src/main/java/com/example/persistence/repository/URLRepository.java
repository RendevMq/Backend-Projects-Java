package com.example.persistence.repository;


import com.example.persistence.entity.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface URLRepository extends JpaRepository<ShortenedUrl, Long> {

    // Encuentra una URL acortada por su código corto
    Optional<ShortenedUrl> findByShortCode(String shortCode);

    // Elimina una URL acortada por su código corto
    void deleteByShortCode(String shortCode);
}
