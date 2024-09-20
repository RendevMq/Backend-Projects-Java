package com.example.persistence.repository;

import com.example.persistence.entity.authEntities.RoleEntity;
import com.example.persistence.entity.authEntities.RoleEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity,Long> {

    //Me trae solo los roles que existan
    List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> rolename);

    // Encontrar un rol por su nombre (enum)
    Optional<RoleEntity> findByRoleEnum(RoleEnum roleEnum);

    // Encontrar múltiples roles por una lista de enums
    List<RoleEntity> findByRoleEnumIn(List<RoleEnum> roleEnums);
}
