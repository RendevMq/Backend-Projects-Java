package com.example.util;
import com.example.presentation.dto.WeatherDTO;
import com.example.presentation.dto.CurrentConditionsDTO;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WeatherMapper {

    public WeatherDTO mapToWeatherDTO(JsonNode weatherNode) {
        WeatherDTO weatherDTO = new WeatherDTO();

        // Mapeo de valores simples
        weatherDTO.setQueryCost(weatherNode.get("queryCost").asInt());
        weatherDTO.setLatitude(weatherNode.get("latitude").asDouble());
        weatherDTO.setLongitude(weatherNode.get("longitude").asDouble());
        weatherDTO.setResolvedAddress(weatherNode.get("resolvedAddress").asText());
        weatherDTO.setAddress(weatherNode.get("address").asText());
        weatherDTO.setTimezone(weatherNode.get("timezone").asText());
        weatherDTO.setTzoffset(weatherNode.get("tzoffset").asDouble());
        weatherDTO.setDescription(weatherNode.get("description").asText());

        // Mapea la parte de currentConditions
        JsonNode currentConditionsNode = weatherNode.get("currentConditions");
        if (currentConditionsNode != null) {
            CurrentConditionsDTO currentConditions = new CurrentConditionsDTO();
            currentConditions.setDatetime(currentConditionsNode.get("datetime").asText());
            currentConditions.setDatetimeEpoch(currentConditionsNode.get("datetimeEpoch").asLong());
            currentConditions.setTemp(currentConditionsNode.get("temp").asDouble());
            currentConditions.setFeelslike(currentConditionsNode.get("feelslike").asDouble());
            currentConditions.setHumidity(currentConditionsNode.get("humidity").asDouble());
            currentConditions.setDew(currentConditionsNode.get("dew").asDouble());
            currentConditions.setPrecip(currentConditionsNode.get("precip").asDouble());
            currentConditions.setPrecipprob(currentConditionsNode.get("precipprob").asDouble());
            currentConditions.setSnow(currentConditionsNode.get("snow").asDouble());
            currentConditions.setSnowdepth(currentConditionsNode.get("snowdepth").asDouble());

            // preciptype puede ser null
            JsonNode preciptypeNode = currentConditionsNode.get("preciptype");
            if (preciptypeNode != null && !preciptypeNode.isNull()) {
                currentConditions.setPreciptype(preciptypeNode.asText());
            }

            currentConditions.setWindgust(currentConditionsNode.get("windgust").asDouble());
            currentConditions.setWindspeed(currentConditionsNode.get("windspeed").asDouble());
            currentConditions.setWinddir(currentConditionsNode.get("winddir").asDouble());
            currentConditions.setPressure(currentConditionsNode.get("pressure").asDouble());
            currentConditions.setVisibility(currentConditionsNode.get("visibility").asDouble());
            currentConditions.setCloudcover(currentConditionsNode.get("cloudcover").asDouble());
            currentConditions.setSolarradiation(currentConditionsNode.get("solarradiation").asDouble());
            currentConditions.setSolarenergy(currentConditionsNode.get("solarenergy").asDouble());
            currentConditions.setUvindex(currentConditionsNode.get("uvindex").asDouble());
            currentConditions.setConditions(currentConditionsNode.get("conditions").asText());
            currentConditions.setIcon(currentConditionsNode.get("icon").asText());

            // Mapea la lista de estaciones
            JsonNode stationsNode = currentConditionsNode.get("stations");
            if (stationsNode != null && stationsNode.isArray()) {
                List<String> stations = new ArrayList<>();
                for (JsonNode stationNode : stationsNode) {
                    stations.add(stationNode.asText());
                }
                currentConditions.setStations(stations);
            }

            currentConditions.setSource(currentConditionsNode.get("source").asText());
            currentConditions.setSunrise(currentConditionsNode.get("sunrise").asText());
            currentConditions.setSunriseEpoch(currentConditionsNode.get("sunriseEpoch").asLong());
            currentConditions.setSunset(currentConditionsNode.get("sunset").asText());
            currentConditions.setSunsetEpoch(currentConditionsNode.get("sunsetEpoch").asLong());
            currentConditions.setMoonphase(currentConditionsNode.get("moonphase").asDouble());

            // Asignar currentConditions a weatherDTO
            weatherDTO.setCurrentConditions(currentConditions);
        }

        return weatherDTO;
    }
}
