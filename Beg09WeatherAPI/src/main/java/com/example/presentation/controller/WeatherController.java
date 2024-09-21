package com.example.presentation.controller;

import com.example.presentation.dto.WeatherDTO;
import com.example.service.implementation.RateLimiterService;
import com.example.service.interfaces.IWeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class WeatherController {

    @Autowired
    private IWeatherService iWeatherService;

    @Autowired
    private RateLimiterService rateLimiterService;

    @GetMapping("/weather/{city}")
    public WeatherDTO getWeatherByCity(@PathVariable String city, @RequestHeader(value = "X-Forwarded-For", required = false) String userIp) {

        // Usar el IP del usuario para el control de la tasa de solicitudes
        String userId = (userIp != null) ? userIp : "default_user";

        if (!rateLimiterService.isAllowed(userId)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Rate limit exceeded");
        }

        return iWeatherService.getWeather(city);
    }

    /*@GetMapping("/weather/{city}")
    public WeatherDTO getWeatherByCity(@PathVariable String city) {
        return iWeatherService.getWeather(city);
    }*/

}
