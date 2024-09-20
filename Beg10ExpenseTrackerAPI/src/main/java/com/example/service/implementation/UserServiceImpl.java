package com.example.service.implementation;

import com.example.persistence.entity.authEntities.UserEntity;
import com.example.persistence.repository.UserRepository;
import com.example.presentation.dto.UserDTO;
import com.example.service.interfaces.IUserService;
import com.example.util.EntityToDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserEntity> users = (List<UserEntity>) userRepository.findAll();
        return users.stream().map(EntityToDTOMapper::mapToUserDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(EntityToDTOMapper::mapToUserDTO);
    }

    @Override
    public Optional<UserDTO> getUserByUsername(String username) {
        return userRepository.findUserEntityByUsername(username).map(EntityToDTOMapper::mapToUserDTO);
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
    public boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    @Override
    public boolean isOwner(Long userId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> currentUser = userRepository.findUserEntityByUsername(currentUsername);
        return currentUser.isPresent() && currentUser.get().getId().equals(userId);
    }
}
