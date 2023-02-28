package com.example.routeplanner.model.api;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;
import java.util.Map;
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class Leg {
    private List<ApiStep> steps;
    private Map<String, String>duration;                //key,value
    private Map<String, String>durationInTraffic;
    private Map<String, String> endLocation;
}
