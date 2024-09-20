package com.example.service.interfaces;

import com.example.presentation.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    // Obtener todos los usuarios
    List<UserDTO> getAllUsers();

    // Eliminar un usuario
    void deleteUser(Long id);

    // Obtener un usuario por su ID
    Optional<UserDTO> getUserById(Long id);

    // Obtener un usuario por nombre de usuario
    Optional<UserDTO> getUserByUsername(String username);

    // Verifica si el usuario autenticado es admin
    boolean isAdmin();

    // Verifica si el usuario autenticado es el propietario del recurso
    boolean isOwner(Long userId);

    // Verificar si existe un usuario por nombre de usuario o email
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
