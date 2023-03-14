package com.example.routeplanner.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiRoute {
    private List<Leg>legs;
    private int id;


}
