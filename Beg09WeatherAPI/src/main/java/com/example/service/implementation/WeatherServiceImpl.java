package com.example.service.implementation;

import com.example.presentation.dto.WeatherDTO;
import com.example.service.http.WeatherClient;
import com.example.service.interfaces.IWeatherService;
import com.example.util.WeatherMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WeatherServiceImpl implements IWeatherService {
    @Autowired
    private WeatherClient weatherClient;

    @Autowired
    private WeatherMapper weatherMapper;

    @Override
    @Cacheable(value = "weathers", key = "#city")
    public WeatherDTO getWeather(String city) {
        String response = weatherClient.getWeatherFromApi(city);

        // Parsear la respuesta de la API
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode weatherNode = objectMapper.readTree(response);
            return weatherMapper.mapToWeatherDTO(weatherNode);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
