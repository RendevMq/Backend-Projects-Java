package com.example.service.implementation;

import com.example.persistence.entity.ShortenedUrl;
import com.example.persistence.repository.URLRepository;
import com.example.presentation.dto.UrlRequestDTO;
import com.example.presentation.dto.UrlResponseDTO;
import com.example.presentation.dto.UrlStatsDTO;
import com.example.service.interfaces.URLShorteningService;
import com.example.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
public class URLShorteningServiceImpl implements URLShorteningService {

    private static final String URL_ACCESS_COUNT_KEY = "url_access_count:";

    @Autowired
    private URLRepository urlRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Formato de fecha
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional
    public UrlResponseDTO createShortUrl(UrlRequestDTO urlRequestDTO) {
        // Generar un código corto único
        String shortCode = generateUniqueShortCode();

        // Crear una nueva entidad ShortenedUrl
        ShortenedUrl shortenedUrl = ShortenedUrl.builder()
                .originalUrl(urlRequestDTO.getOriginalUrl())
                .shortCode(shortCode)
                .build();

        // Guardar en la base de datos
        ShortenedUrl savedUrl = urlRepository.save(shortenedUrl);

        // Devolver la respuesta
        return UrlResponseDTO.builder()
                .originalUrl(savedUrl.getOriginalUrl())
                .shortCode(savedUrl.getShortCode())
                .shortUrl("http://localhost:8080/shorten/" + savedUrl.getShortCode())
                .createdAt(savedUrl.getCreatedAt().format(formatter))
                .build();
    }

    @Override
    public UrlResponseDTO getOriginalUrl(String shortCode) {
        // Buscar en la base de datos por el código corto
        ShortenedUrl shortenedUrl = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));

        // Incrementar el contador de accesos en Redis
        redisTemplate.opsForValue().increment(URL_ACCESS_COUNT_KEY + shortCode);

        // Devolver la respuesta
        return UrlResponseDTO.builder()
                .originalUrl(shortenedUrl.getOriginalUrl())
                .shortCode(shortenedUrl.getShortCode())
                .shortUrl("http://localhost:8080/" + shortenedUrl.getShortCode())
                .createdAt(shortenedUrl.getCreatedAt().format(formatter))
                .build();
    }

    @Override
    @Transactional
    public void deleteShortUrl(String shortCode) {
        // Eliminar la URL corta de la base de datos
        Optional<ShortenedUrl> shortenedUrl = urlRepository.findByShortCode(shortCode);
        if (shortenedUrl.isPresent()) {
            urlRepository.deleteByShortCode(shortCode);
        } else {
            throw new ResourceNotFoundException("Short URL not found");
        }

        // Eliminar el contador de accesos en Redis
        redisTemplate.delete(URL_ACCESS_COUNT_KEY + shortCode);
    }

    @Override
    public UrlStatsDTO getUrlStats(String shortCode) {
        // Buscar en la base de datos
        ShortenedUrl shortenedUrl = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));

        // Obtener el número de accesos desde Redis
        Long accessCount = (Long) redisTemplate.opsForValue().get(URL_ACCESS_COUNT_KEY + shortCode);
        accessCount = accessCount != null ? accessCount : 0;

        // Devolver las estadísticas
        return UrlStatsDTO.builder()
                .shortCode(shortCode)
                .accessCount(accessCount)
                .originalUrl(shortenedUrl.getOriginalUrl())
                .createdAt(shortenedUrl.getCreatedAt().format(formatter))
                .build();
    }

    // Generar un código corto único
    private String generateUniqueShortCode() {
        // Puedes usar UUID o cualquier otra lógica para generar códigos únicos
        return UUID.randomUUID().toString().substring(0, 6);
    }
}
