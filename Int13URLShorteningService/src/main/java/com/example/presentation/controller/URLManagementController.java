package com.example.presentation.controller;

import com.example.service.interfaces.URLShorteningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shorten")
public class URLManagementController {

    @Autowired
    private URLShorteningService urlShorteningService;

    // Eliminar una URL corta
    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> deleteShortUrl(@PathVariable String shortCode) {
        urlShorteningService.deleteShortUrl(shortCode);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content si se elimina con éxito
    }
}
