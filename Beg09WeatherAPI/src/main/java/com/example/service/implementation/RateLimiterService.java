package com.example.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimiterService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final int MAX_REQUESTS = 10; // Límite de 10 solicitudes
    private static final long WINDOW_TIME = 60;  // 60 segundos

    public boolean isAllowed(String userId) {
        String redisKey = "rate_limit:" + userId;

        // Incrementa el contador de solicitudes
        Long requests = redisTemplate.opsForValue().increment(redisKey);

        // Si es la primera solicitud, establece la expiración de la ventana de tiempo
        if (requests == 1) {
            redisTemplate.expire(redisKey, WINDOW_TIME, TimeUnit.SECONDS);
        }

        // Comprueba si el usuario ha excedido el límite de solicitudes
        return requests != null && requests <= MAX_REQUESTS;
    }
}
