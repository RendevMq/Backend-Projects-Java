package com.example.service.interfaces;

import com.example.presentation.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    // Crear o guardar un nuevo usuario
    //UserDTO save(UserDTO userDTO);

    // Buscar todos los usuarios
    List<UserDTO> findAll();

    // Buscar un usuario por su ID
    Optional<UserDTO> findById(Long id);

    // Buscar un usuario por su nombre de usuario
    Optional<UserDTO> findByUsername(String username);

    // Buscar un usuario por su email
    Optional<UserDTO> findByEmail(String email);

    // Verificar si un nombre de usuario ya existe
    boolean existsByUsername(String username);

    // Verificar si un email ya existe
    boolean existsByEmail(String email);

    // Eliminar un usuario por su ID
    void deleteById(Long id);
}
