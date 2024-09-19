package com.example.service.interfaces;

import com.example.presentation.dto.TagDTO;

import java.util.List;
import java.util.Optional;

public interface ITagService {
    // Crear o guardar una nueva etiqueta
    TagDTO save(TagDTO tagDTO);

    // Buscar todas las etiquetas
    List<TagDTO> findAll();

    // Buscar una etiqueta por su ID
    Optional<TagDTO> findById(Long id);

    // Buscar una etiqueta por su nombre
    Optional<TagDTO> findByName(String name);

    // Eliminar una etiqueta por su ID
    void deleteById(Long id);
}
