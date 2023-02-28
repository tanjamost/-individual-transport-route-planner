package com.example.routeplanner.model.api;

import com.example.routeplanner.model.api.ApiRoute;
import lombok.Data;

import java.util.List;
@Data
public class ApiResponse {
    List<ApiRoute> routes;
}
