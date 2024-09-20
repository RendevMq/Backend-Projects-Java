package com.example;
import com.example.persistence.entity.authEntities.RoleEntity;
import com.example.persistence.entity.authEntities.RoleEnum;
import com.example.persistence.entity.authEntities.UserEntity;
import com.example.persistence.repository.RoleRepository;
import com.example.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class Beg10ExpenseTrackerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(Beg10ExpenseTrackerApiApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// 1) Creamos los roles
			RoleEntity roleAdmin = RoleEntity.builder()
					.roleEnum(RoleEnum.ADMIN)
					.build();
			RoleEntity roleUser = RoleEntity.builder()
					.roleEnum(RoleEnum.USER)
					.build();

			// Guardamos los roles en la base de datos si no existen
			roleRepository.saveAll(List.of(roleAdmin, roleUser));

			// Recuperamos los roles desde la base de datos después de guardarlos
			RoleEntity savedRoleAdmin = roleRepository.findByRoleEnum(RoleEnum.ADMIN)
					.orElseThrow(() -> new RuntimeException("Role ADMIN no encontrado"));
			RoleEntity savedRoleUser = roleRepository.findByRoleEnum(RoleEnum.USER)
					.orElseThrow(() -> new RuntimeException("Role USER no encontrado"));

			// 2) Creamos los usuarios
			UserEntity adminUser = UserEntity.builder()
					.username("admin")
					.password(passwordEncoder.encode("admin"))  // Cambiar a una contraseña real
					.isEnable(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(savedRoleAdmin))  // Asignamos rol de ADMIN
					.build();

			UserEntity regularUser = UserEntity.builder()
					.username("user")
					.password(passwordEncoder.encode("user"))  // Cambiar a una contraseña real
					.isEnable(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(savedRoleUser))  // Asignamos rol de USER
					.build();

			// Guardamos los usuarios en la base de datos
			userRepository.saveAll(List.of(adminUser, regularUser));
		};
	}
}
