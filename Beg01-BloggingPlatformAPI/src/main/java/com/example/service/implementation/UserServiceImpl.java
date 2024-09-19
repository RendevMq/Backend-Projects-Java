package com.example.service.implementation;

import com.example.persistence.entity.authEntities.UserEntity;
import com.example.persistence.reposiroty.authRepositories.UserRepository;
import com.example.presentation.dto.UserDTO;
import com.example.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserDTO> findAll() {
        List<UserEntity> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add); // Convertir Iterable a List
        return users.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> findById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToDTO);
    }

    @Override
    public Optional<UserDTO> findByUsername(String username) {
        return userRepository.findUserEntityByUsername(username)
                .map(this::mapToDTO);
    }

    @Override
    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::mapToDTO);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    // Método auxiliar para mapear UserEntity a UserDTO
    private UserDTO mapToDTO(UserEntity user) {
        List<String> roleNames = user.getRoles().stream()
                .map(role -> role.getRoleEnum().name()) // O usa el getter adecuado para obtener el nombre del rol
                .collect(Collectors.toList());

        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), roleNames);
    }
}


