package com.example.routeplanner.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class Weather {

    private double temperature;

    private double windSpeed;

    private double temperatureFeelsLike;

    private String description;
}
