package com.example.persistence.repository;

import com.example.persistence.entity.authEntities.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

    // Buscar un usuario por su email
    Optional<UserEntity> findByEmail(String email);

    // Verificar si existe un usuario con un email específico
    boolean existsByEmail(String email);

    // Buscar un usuario por su username (para autenticación)
    Optional<UserEntity> findUserEntityByName(String username);
}
