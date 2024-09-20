package com.example.presentation.dto.authDto;


import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated //necesaria para validar el rolerequest
public record AuthCreateRoleRequest(
        @Size(max = 4 , message = "The user cannot have more than 4 roles gaaa")List<String> roleListName) {
}
