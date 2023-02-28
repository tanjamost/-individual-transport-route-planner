package com.example.routeplanner.model.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CurrentWeather {

    private List<Map<String,String>> weather;
    private double temp;
    private double feelsLike;
    private double windSpeed;





}
