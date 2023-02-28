package com.example.routeplanner.controller;

import com.example.routeplanner.model.Route;
import com.example.routeplanner.service.CarRouteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class CarRouteController {
    private CarRouteService carRouteService;
    @GetMapping("/driving")
    public List<Route> getCarRoute(@RequestParam String origin, @RequestParam String destination){
        return carRouteService.getRoutes(destination, origin);
    }
}