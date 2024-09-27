package com.example.presentation.controller;


import com.example.presentation.dto.UrlRequestDTO;
import com.example.presentation.dto.UrlResponseDTO;
import com.example.service.interfaces.URLShorteningService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/shorten")
public class URLShorteningController {

    @Autowired
    private URLShorteningService urlShorteningService;

    // Crear una nueva URL corta
    @PostMapping
    public ResponseEntity<UrlResponseDTO> createShortUrl(@Valid @RequestBody UrlRequestDTO urlRequestDTO) {
        UrlResponseDTO response = urlShorteningService.createShortUrl(urlRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Redirigir desde una URL corta a la URL original
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode) {
        // Buscar la URL original a partir del código corto
        UrlResponseDTO response = urlShorteningService.getOriginalUrl(shortCode);

        // Redirigir a la URL original
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, response.getOriginalUrl())
                .build();
    }
}
