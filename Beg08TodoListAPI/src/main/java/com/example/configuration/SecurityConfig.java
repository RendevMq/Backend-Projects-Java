package com.example.configuration;

import com.example.service.implementation.UserDetailsServiceImpl;
import com.example.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenValidator jwtTokenValidator;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private JwtUtils jwtUtils;
    // Constructor-based injection (sin UserDetailsServiceImpl para evitar el ciclo)
    public SecurityConfig(JwtTokenValidator jwtTokenValidator, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtTokenValidator = jwtTokenValidator;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Deshabilitamos CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()  // Permitimos acceso a estos endpoints
                        .anyRequest().authenticated()  // Cualquier otra solicitud requiere autenticación
                )
                /*.exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)  // Manejo de errores 401
                        .accessDeniedHandler(accessDeniedHandler())  // Manejo de errores 403 si no tiene permisos
                )*/
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class);  // Añadimos el filtro JWT
                //.addFilterBefore(jwtTokenValidator, UsernamePasswordAuthenticationFilter.class);  // Añadimos el filtro JWT (tambien cumple)

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsServiceImpl) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsServiceImpl);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
