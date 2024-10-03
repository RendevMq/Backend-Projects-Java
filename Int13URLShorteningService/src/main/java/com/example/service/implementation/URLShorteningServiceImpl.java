package com.example.service.implementation;

import com.example.persistence.entity.ShortenedUrl;
import com.example.persistence.repository.URLRepository;
import com.example.presentation.dto.UrlRequestDTO;
import com.example.presentation.dto.UrlResponseDTO;
import com.example.presentation.dto.UrlStatsDTO;
import com.example.service.interfaces.URLShorteningService;
import com.example.service.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class URLShorteningServiceImpl implements URLShorteningService {

    private static final Logger logger = LoggerFactory.getLogger(URLShorteningServiceImpl.class);

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
        // Verificar si la URL ya ha sido acortada
        Optional<ShortenedUrl> existingUrl = urlRepository.findByOriginalUrl(urlRequestDTO.getOriginalUrl());
        if (existingUrl.isPresent()) {
            ShortenedUrl shortenedUrl = existingUrl.get();
            // Devolver el código corto existente
            return UrlResponseDTO.builder()
                    .originalUrl(shortenedUrl.getOriginalUrl())
                    .shortCode(shortenedUrl.getShortCode())
                    .shortUrl("http://localhost:8080/shorten/" + shortenedUrl.getShortCode())
                    .createdAt(shortenedUrl.getCreatedAt().format(formatter))
                    .build();
        }

        // Si no existe, generar un nuevo código corto
        String shortCode = generateUniqueShortCode();

        ShortenedUrl shortenedUrl = ShortenedUrl.builder()
                .originalUrl(urlRequestDTO.getOriginalUrl())
                .shortCode(shortCode)
                .build();

        // Guardar en la base de datos
        ShortenedUrl savedUrl = urlRepository.save(shortenedUrl);

        // Devolver la respuesta con la nueva URL corta
        return UrlResponseDTO.builder()
                .originalUrl(savedUrl.getOriginalUrl())
                .shortCode(savedUrl.getShortCode())
                .shortUrl("http://localhost:8080/shorten/" + savedUrl.getShortCode())
                .createdAt(savedUrl.getCreatedAt().format(formatter))
                .build();
    }

    @Override
    public UrlResponseDTO getOriginalUrl(String shortCode) {
        // Siempre incrementar el contador, aunque el resultado esté cacheado
        redisTemplate.opsForValue().increment(URL_ACCESS_COUNT_KEY + shortCode);

        // Luego obtener la URL, ya sea desde la base de datos o desde el caché
        return getOriginalUrlWithoutIncrement(shortCode);
    }

    @Cacheable(value = "shortUrlCache", key = "#shortCode") // Cachear el resultado de getOriginalUrl
    public UrlResponseDTO getOriginalUrlWithoutIncrement(String shortCode) {
        // Buscar en la base de datos por el código corto
        ShortenedUrl shortenedUrl = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));

        logger.info("Se encontró la URL original para el shortCode: {}", shortenedUrl); // Solo se tiene que ver la primera vez

        // Devolver la respuesta
        return UrlResponseDTO.builder()
                .originalUrl(shortenedUrl.getOriginalUrl())
                .shortCode(shortenedUrl.getShortCode())
                .shortUrl("http://localhost:8080/" + shortenedUrl.getShortCode())
                .createdAt(shortenedUrl.getCreatedAt().format(formatter))
                .build();
    }

    /*
    @Override
    @Cacheable(value = "shortUrlCache", key = "#shortCode") // Cachear el resultado de getOriginalUrl
    public UrlResponseDTO getOriginalUrl(String shortCode) {

        // Buscar en la base de datos por el código corto
        ShortenedUrl shortenedUrl = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));

        logger.info("Se encontró la URL original para el shortCode: {}", shortCode); // Solo se tiene que ver la primera vez


        // Siempre incrementar el contador, aunque el resultado esté cacheado
        redisTemplate.opsForValue().increment(URL_ACCESS_COUNT_KEY + shortCode);

        // Devolver la respuesta
        return UrlResponseDTO.builder()
                .originalUrl(shortenedUrl.getOriginalUrl())
                .shortCode(shortenedUrl.getShortCode())
                .shortUrl("http://localhost:8080/" + shortenedUrl.getShortCode())
                .createdAt(shortenedUrl.getCreatedAt().format(formatter))
                .build();
    }*/


    @Override
    public List<UrlResponseDTO> getAllUrls() {
        // Recuperar la lista de URLs acortadas
        List<ShortenedUrl> shortenedUrlList = urlRepository.findAll();

        // Convertir a la lista de UrlResponseDTOs con el número de accesos , tmb se puede con streams
        List<UrlResponseDTO> urlResponseDTOList = new ArrayList<>();
        for (ShortenedUrl url : shortenedUrlList) {
            // Obtener el número de accesos desde Redis
            Object accessCountObj = redisTemplate.opsForValue().get(URL_ACCESS_COUNT_KEY + url.getShortCode());
            Long accessCount = 0L;
            if (accessCountObj instanceof Integer) {
                accessCount = ((Integer) accessCountObj).longValue();
            } else if (accessCountObj instanceof Long) {
                accessCount = (Long) accessCountObj;
            }

            // Agregar la URL a la lista de respuesta con el número de accesos
            UrlResponseDTO urlResponseDTO = UrlResponseDTO.builder()
                    .originalUrl(url.getOriginalUrl())
                    .shortCode(url.getShortCode())
                    .shortUrl("http://localhost:8080/shorten/" + url.getShortCode())
                    .createdAt(url.getCreatedAt().format(formatter))
                    .accessCount(accessCount)  // Añadir el número de accesos
                    .build();

            urlResponseDTOList.add(urlResponseDTO);
        }
        return urlResponseDTOList;
    }

    @Override
    @Transactional
    @CacheEvict(value = "shortUrlCache", key = "#shortCode") // Limpiar el caché cuando se elimine una URL
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
        Object accessCountObj = redisTemplate.opsForValue().get(URL_ACCESS_COUNT_KEY + shortCode);
        // Manejar tanto Integer como Long
        Long accessCount = 0L;
        if (accessCountObj instanceof Integer) {
            accessCount = ((Integer) accessCountObj).longValue();
        } else if (accessCountObj instanceof Long) {
            accessCount = (Long) accessCountObj;
        }

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
