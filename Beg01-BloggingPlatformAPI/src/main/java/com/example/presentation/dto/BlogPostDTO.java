package com.example.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogPostDTO {

    private Long id;
    private String title;
    private String content;
    private Long userId;  // Solo el ID del usuario
    private Long categoryId;  // Solo el ID de la categoría
    private Set<String> tags;  // Solo los nombres de los tags
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
