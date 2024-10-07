package com.example.presentation.controller;

import com.example.presentation.dto.UrlStatsDTO;
import com.example.service.interfaces.URLShorteningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/shorten")
public class URLStatsController {

    @Autowired
    private URLShorteningService urlShorteningService;

    // Obtener estadísticas de acceso para una URL corta
    @GetMapping("/{shortCode}/stats")
    public ResponseEntity<UrlStatsDTO> getUrlStats(@PathVariable String shortCode) {
        UrlStatsDTO stats = urlShorteningService.getUrlStats(shortCode);
        return ResponseEntity.ok(stats);
    }
}
