package com.example;

import com.example.persistence.entity.authEntities.PermissionEntity;
import com.example.persistence.entity.authEntities.RoleEntity;
import com.example.persistence.entity.authEntities.RoleEnum;
import com.example.persistence.entity.authEntities.UserEntity;
import com.example.persistence.reposiroty.authRepositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class Beg07BloggingPlatformApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(Beg07BloggingPlatformApiApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository) {//Este metodo se ejecuta al levantar la aplicacion
		// y la usaremos para que al levantar la aplicacion se ejecutara este codigo para guardar registros inicales en la base de datos

		//userRepository, encargado de registar los usuarios y demas.. lo inyectamos aprovechando el method inyection
		return args -> {

			// 1) Creamos los permisos
			PermissionEntity createPermission = PermissionEntity.builder()
					.name("CREATE")
					.build();

			PermissionEntity readPermission = PermissionEntity.builder()
					.name("READ")
					.build();

			PermissionEntity updatePermission = PermissionEntity.builder()
					.name("UPDATE")
					.build();

			PermissionEntity deletePermission = PermissionEntity.builder()
					.name("DELETE")
					.build();


			// 2) Creamos los roles
			RoleEntity roleAdmin = RoleEntity.builder()
					.roleEnum(RoleEnum.ADMIN)
					.permissionEntitySet(Set.of(createPermission, readPermission, updatePermission, deletePermission))
					.build();
			RoleEntity roleUser = RoleEntity.builder()
					.roleEnum(RoleEnum.USER)
					.permissionEntitySet(Set.of(createPermission, readPermission, updatePermission, deletePermission))
					.build();
			RoleEntity roleInvited = RoleEntity.builder()
					.roleEnum(RoleEnum.INVITED)
					.permissionEntitySet(Set.of(readPermission))
					.build();


			// 3) Creamos los usuarios
			UserEntity userRenato = UserEntity.builder()
					.username("jose")
					.password("$2a$10$qXCq.ZY4t8MWFYZmbZxKIO4EbDkUAZ3EDDvhvfeOZXs88/7027hH6")
					.isEnable(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleAdmin))
					.build();

			UserEntity userDaniel = UserEntity.builder()
					.username("daniel")
					.password("$2a$10$qXCq.ZY4t8MWFYZmbZxKIO4EbDkUAZ3EDDvhvfeOZXs88/7027hH6")
					.isEnable(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleUser))
					.build();

			UserEntity userAndrea = UserEntity.builder()
					.username("andrea")
					.password("$2a$10$qXCq.ZY4t8MWFYZmbZxKIO4EbDkUAZ3EDDvhvfeOZXs88/7027hH6")
					.isEnable(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleInvited))
					.build();


			//GUARMOS USER Y GRACIAS A LA PROPIEDAD DE CASCADA SE GUARDA LO DEMAS
			userRepository.saveAll(List.of(userRenato,userDaniel,userAndrea));
		};

	}
}
