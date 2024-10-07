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
//@RequestMapping("/shorten")
public class URLShorteningController {

    @Autowired
    private URLShorteningService urlShorteningService;

    // Crear una nueva URL corta
    @PostMapping
    public ResponseEntity<UrlResponseDTO> createShortUrl(@Valid @RequestBody UrlRequestDTO urlRequestDTO) {
        UrlResponseDTO response = urlShorteningService.createShortUrl(urlRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode) {
        // Buscar la URL original a partir del código corto
        UrlResponseDTO response = urlShorteningService.getOriginalUrl(shortCode);

        // Desactivar el caché en la respuesta
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Location", response.getOriginalUrl());

        // Redirigir a la URL original
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .headers(headers)
                .build();
    }

    @GetMapping("/findAll")
    public ResponseEntity<?>  findAll(){
        return ResponseEntity.ok(urlShorteningService.getAllUrls());
    }
}
