package com.example.service.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherClient {

    private static final String BASE_LEFT_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
    private static final String BASE_CENTER_URL = "?unitGroup=us&key=";
    private static final String BASE_RIGHT_URL = "&contentType=json";

    @Value("${security.api.key.private}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    public String getWeatherFromApi(String city) {
        String url = BASE_LEFT_URL + city + BASE_CENTER_URL + apiKey + BASE_RIGHT_URL ;
        return restTemplate.getForObject(url, String.class);
    }
}
