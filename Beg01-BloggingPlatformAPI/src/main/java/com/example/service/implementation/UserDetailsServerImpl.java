package com.example.service.implementation;

import com.example.persistence.entity.authEntities.RoleEntity;
import com.example.persistence.entity.authEntities.UserEntity;
import com.example.persistence.reposiroty.authRepositories.RoleRepository;
import com.example.persistence.reposiroty.authRepositories.UserRepository;
import com.example.presentation.dto.authDto.AuthCreateUserRequest;
import com.example.presentation.dto.authDto.AuthLoginRequest;
import com.example.presentation.dto.authDto.AuthResponse;
import com.example.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//CREAREMOS NUESTRO "UserDetailsService" PERSONALIZADO QUE SE CONTECTE A LA BASE DE DATOS
//Y TRAIGA LOS USUARIOS

@Service
public class UserDetailsServerImpl implements UserDetailsService {

    //============INYECCIONES=============//
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    //==========================================//
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //BUSCAMOS  el usuario por nombre EN LA BASE DE DATOS
        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe"));
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        userEntity.getRoles()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermissionEntitySet().stream())
                .forEach(permissionEntity -> authorityList.add(new SimpleGrantedAuthority(permissionEntity.getName())));

        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnable(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialNoExpired(),
                userEntity.isAccountNoLocked(),
                authorityList);
    }

    //================================================================//
    //====METODO CON EL CUAL ME PUEDA LOGUEAR====//
    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {
        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        //ahora nos autenticamos
        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.createToken(authentication);

        //devolvemos
        AuthResponse authResponse = new AuthResponse(username, "user loged successfully gaa", accessToken, true);
        return authResponse;
    }

    //====IMPLEMENTAMOS EL METODO AUTHENTICATE, SE ENCARGARA DE QUE LAS CREDENCIALES EAN CORRECTAS====//
    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password GAA");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }
    //================================================================//

    public AuthResponse createUser(AuthCreateUserRequest authCreateUserRequest) {

        //OBTENEMOS Los datos del iusuraio
        String username = authCreateUserRequest.username();
        String password = authCreateUserRequest.password();

        List<String> roleRequest = authCreateUserRequest.roleRequest().roleListName();
        Set<RoleEntity> roleEntitySet = roleRepository.findRoleEntitiesByRoleEnumIn(roleRequest).stream().collect(Collectors.toSet());

        if (roleEntitySet.isEmpty()) {
            throw new IllegalArgumentException("The roles specified does not exist");
        }

        //si hay por lo menos un rol que existe y valido
        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password)) //encriptamos
                .roles(roleEntitySet)
                .isEnable(true)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .credentialNoExpired(true)
                .build();

        UserEntity userCreated = userRepository.save(userEntity); //con esto creamos el usuario en la BASE DE DATOS

        //neceito una lista con los permisos que va a tener
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        userCreated.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        //seteamos los permisos
        userCreated.getRoles().stream()
                .flatMap(role -> role.getPermissionEntitySet().stream())
                .forEach(permissionEntity -> authorityList.add(new SimpleGrantedAuthority(permissionEntity.getName())));

        //momento de darle acceso
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getUsername() , userCreated.getPassword() ,authorityList);

        //generamos el token
        String accesToken = jwtUtils.createToken(authentication);

        //damos la respuesta
        AuthResponse authResponse = new AuthResponse(userCreated.getUsername() , "User created successfully gaa" , accesToken, true);
        return authResponse;
    }


}

