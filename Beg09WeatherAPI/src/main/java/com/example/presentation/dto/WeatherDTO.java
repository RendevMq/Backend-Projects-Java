package com.example.presentation.dto;
import lombok.Data;

import java.io.Serializable;

@Data
public class WeatherDTO implements Serializable {
    private int queryCost;
    private double latitude;
    private double longitude;
    private String resolvedAddress;
    private String address;
    private String timezone;
    private double tzoffset;
    private String description;
    private CurrentConditionsDTO currentConditions;
}
