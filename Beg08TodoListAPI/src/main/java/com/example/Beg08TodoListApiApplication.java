package com.example;

import com.example.persistence.entity.authEntities.UserEntity;
import com.example.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class Beg08TodoListApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(Beg08TodoListApiApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// Verificamos si ya existen usuarios en la base de datos
			if (userRepository.count() == 0) {
				// Creamos el primer usuario: admin
				UserEntity adminUser = UserEntity.builder()
						.email("admin@example.com")
						.password(passwordEncoder.encode("admin123"))  // Contraseña hasheada
						.name("Admin User")
						.isEnable(true)
						.accountNoExpired(true)
						.accountNoLocked(true)
						.credentialNoExpired(true)
						.build();

				// Creamos el segundo usuario: regular user
				UserEntity regularUser = UserEntity.builder()
						.email("user@example.com")
						.password(passwordEncoder.encode("user123"))  // Contraseña hasheada
						.name("Regular User")
						.isEnable(true)
						.accountNoExpired(true)
						.accountNoLocked(true)
						.credentialNoExpired(true)
						.build();

				// Guardamos los usuarios en MongoDB
				userRepository.saveAll(List.of(adminUser, regularUser));
			}
		};
	}
}
