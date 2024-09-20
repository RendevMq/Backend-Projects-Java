package com.example.persistence.repository;

import com.example.persistence.entity.authEntities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findUserEntityByUsername(String username);

    // Encontrar usuario por email
    Optional<UserEntity> findByEmail(String email);

    // Verificar si existe un usuario con un nombre de usuario
    boolean existsByUsername(String username);

    // Verificar si existe un usuario con un email
    boolean existsByEmail(String email);

}
