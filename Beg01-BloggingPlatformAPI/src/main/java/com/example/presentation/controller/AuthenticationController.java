package com.example.presentation.controller;

//CONTROLADOR CON EL CUAL EL CLIENTE SE PUEDA AUTENTICAR

import com.example.presentation.dto.authDto.AuthCreateUserRequest;
import com.example.presentation.dto.authDto.AuthLoginRequest;
import com.example.presentation.dto.authDto.AuthResponse;
import com.example.service.implementation.UserDetailsServerImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserDetailsServerImpl userDetailsService;

    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login (@RequestBody @Valid AuthLoginRequest userRequest){
        return new ResponseEntity<>(this.userDetailsService.loginUser(userRequest), HttpStatus.OK);
    }

    //nuevo metodo para registo
    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register (@RequestBody @Valid AuthCreateUserRequest authCreateUser){
        return new ResponseEntity<>(this.userDetailsService.createUser(authCreateUser), HttpStatus.CREATED);
    }

}