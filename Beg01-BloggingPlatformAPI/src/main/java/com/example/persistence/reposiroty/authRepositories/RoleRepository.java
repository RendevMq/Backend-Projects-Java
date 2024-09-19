package com.example.persistence.reposiroty.authRepositories;

import com.example.persistence.entity.authEntities.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//ESTA INTERFAZ NOS SERVIAR PARA TRAERNOS TODOS LOS ROLES DE NUESTRA BASE DE DATOS PARA PODER COMPARARLOS

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity,Long> {

    //Me trae solo los roles que existan
    List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> rolename);

}
