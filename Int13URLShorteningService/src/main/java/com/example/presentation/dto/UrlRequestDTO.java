package com.example.presentation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlRequestDTO implements Serializable {

    @NotEmpty(message = "URL cannot be empty")
    @Pattern(regexp = "^(http|https)://.*$", message = "URL must be valid")
    private String originalUrl;
}
