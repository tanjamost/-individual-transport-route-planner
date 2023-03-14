package com.example.routeplanner.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Route {                                //final route
    private List<Step> steps;
    private String duration;
    private String durationInTraffic;
    private String arrivalTime;
    private Weather weather;
    private int id;

}
