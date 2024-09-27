package com.example.presentation.dto;

//Para devolver estadísticas de la URL

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlStatsDTO {

    private String shortCode;
    private Long accessCount;
    private String originalUrl;
    private String createdAt;
}
