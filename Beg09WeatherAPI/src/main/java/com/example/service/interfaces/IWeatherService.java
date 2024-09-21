package com.example.service.interfaces;

import com.example.presentation.dto.WeatherDTO;

public interface IWeatherService {
    WeatherDTO getWeather(String city);
}
