package com.example.routeplanner.controller;

import com.example.routeplanner.model.Route;
import com.example.routeplanner.model.api.ApiRoute;
import com.example.routeplanner.service.WalkCycleRouteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class WalkCycleRouteController {
    private WalkCycleRouteService walkCycleRouteService;
    @GetMapping("/walking")
    public List<Route> WalkRoute (@RequestParam String origin, @RequestParam String destination){

        return walkCycleRouteService.getRoutes("walking", destination, origin);
    }
    @GetMapping("/cycling")
    public List<Route> CycleRoute (@RequestParam String origin, @RequestParam String destination){

        return walkCycleRouteService.getRoutes("bicycling", destination, origin);
    }

}