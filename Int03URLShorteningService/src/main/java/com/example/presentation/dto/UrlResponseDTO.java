package com.example.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlResponseDTO {

    private String originalUrl;
    private String shortCode;
    private String shortUrl;  // Ejemplo: http://localhost:8080/{shortCode}
    private String createdAt;
}
