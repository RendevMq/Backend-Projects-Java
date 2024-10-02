package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Int13UrlShorteningServiceApplication {

	// Inyectar el valor de la propiedad desde application.properties o application.yml
	@Value("${cors.allowed.origins}")
	private String allowedOrigins;

	public static void main(String[] args) {
		SpringApplication.run(Int13UrlShorteningServiceApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				// Aplicar CORS a todas las rutas ("/**") y permitir el origen especificado en allowedOrigins
				registry.addMapping("/**")
						.allowedOrigins(allowedOrigins)  // Usar la variable inyectada
						.allowedMethods("*")  // Permitir todos los métodos HTTP (GET, POST, etc.)
						.allowedHeaders("*");  // Permitir todos los headers
			}
		};
	}
}
