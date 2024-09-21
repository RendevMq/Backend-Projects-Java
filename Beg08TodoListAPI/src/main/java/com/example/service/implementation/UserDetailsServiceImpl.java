package com.example.service.implementation;

import com.example.persistence.entity.authEntities.UserEntity;
import com.example.persistence.repository.UserRepository;
import com.example.presentation.dto.authDto.AuthCreateUserRequest;
import com.example.presentation.dto.authDto.AuthLoginRequest;
import com.example.presentation.dto.authDto.AuthResponse;
import com.example.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email no puede estar vacío");
        }

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con email " + email + " no existe"));

        return new org.springframework.security.core.userdetails.User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.isEnable(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialNoExpired(),
                userEntity.isAccountNoLocked(),
                List.of()  // Lista vacía ya que no hay roles
        );
    }

    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {
        if (authLoginRequest.email() == null || authLoginRequest.password() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email y la contraseña son obligatorios");
        }

        String email = authLoginRequest.email();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponse(email, "User logged in successfully", accessToken, true);
    }

    public Authentication authenticate(String email, String password) {
        UserDetails userDetails = this.loadUserByUsername(email);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Contraseña inválida");
        }
        return new UsernamePasswordAuthenticationToken(email, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public AuthResponse createUser(AuthCreateUserRequest authCreateUserRequest) {
        if (authCreateUserRequest.email() == null || authCreateUserRequest.password() == null || authCreateUserRequest.name() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todos los campos (email, nombre, contraseña) son obligatorios");
        }

        String email = authCreateUserRequest.email();
        String password = authCreateUserRequest.password();
        String name = authCreateUserRequest.name();

        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .isEnable(true)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .credentialNoExpired(true)
                .build();

        UserEntity userCreated = userRepository.save(userEntity);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getEmail(), userCreated.getPassword(), List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponse(userCreated.getEmail(), "User created successfully", accessToken, true);
    }
}
