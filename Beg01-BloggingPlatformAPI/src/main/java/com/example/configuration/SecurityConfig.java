package com.example.configuration;

import com.example.configuration.Filter.JwtTokenValidator;
import com.example.service.implementation.UserDetailsServerImpl;
import com.example.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtils jwtUtils;

    //CONFIUGRAMOS EL SECURITY FILTER CHAIN
    //httpSecurity es el objeto que pasa por todos los filtros
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    // Configurar los endpoints PUBLICOS
                    http.requestMatchers(HttpMethod.POST, "/auth/**").permitAll(); //hacemos publicos todos los endpoints de auth porque cuando nos vamos a loguear solo mandamos por el body y no por AUTHORIZATION.. no entendi 1:18:00 -1:24:00

                    // Cofnigurar los endpoints PRIVADOS
                    http.requestMatchers(HttpMethod.POST, "/api/posts").hasAnyRole("ADMIN", "USER");
                    http.requestMatchers(HttpMethod.PUT, "/api/posts/{id}").hasAnyRole("ADMIN", "USER");
                    http.requestMatchers(HttpMethod.DELETE, "/api/posts/{id}").hasAnyRole("ADMIN", "USER");
                    http.requestMatchers(HttpMethod.GET, "/api/posts/{id}").hasAnyAuthority("READ");
                    //http.requestMatchers(HttpMethod.GET, "/method/get").hasAnyRole("INVITED");
                    http.requestMatchers(HttpMethod.GET, "/api/posts").hasAnyAuthority("READ");

                    // Configurar el resto de endpoint - NO ESPECIFICADOS
                    http.anyRequest().denyAll();
                })
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)//Agregamos el filtro, y queremos que se ejecute el filtor de JWT antes de que se ejecute el filtro de Autenticacion (Basic Authentication)
                .build();
    }

    //EL AUTHENTICATION MANAGER PUEDE TENER DIFERENTES PROVIDERS
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServerImpl userDetailsServer) {

        //El autentication tiene muchas implementaciones, en este caso usaremos el del DAO
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder() );
        //provider.setUserDetailsService(userDetailsService());
        provider.setUserDetailsService(userDetailsServer);

        return provider;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {

        //return NoOpPasswordEncoder.getInstance(); //NO CODIFICA, SOLO PARA PRUEBAS POR SEGURIDAD
        return new BCryptPasswordEncoder();
    }

    //================PARA ENCRIPTAR===============//
    //    public static void main(String [] args) {
    //        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    //    }

}

