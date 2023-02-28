package com.example.routeplanner.model.api;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Map;
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class ApiStep {
    private Map<String, String> distance;

    private String htmlInstructions;
}
