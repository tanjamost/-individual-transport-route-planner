package com.example.routeplanner.controller;

import com.example.routeplanner.model.Route;
import com.example.routeplanner.service.FavoriteRouteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/favorite")
public class FavoriteRoutesController {
    private FavoriteRouteService favoriteRouteService;
    @PostMapping
    public void saveRoute(@RequestParam String destination, @RequestParam String origin, @RequestParam String mode,  @RequestParam String username){
        favoriteRouteService.saveRoute(destination, origin, mode, username);
    }
    @GetMapping
    public List<Route> getRoutes(@RequestParam String username){
        return favoriteRouteService.getRoutesByUsername(username);
    }
    @DeleteMapping("/{id}")
    public void removeRoute(@PathVariable int id, @RequestParam String username){
        favoriteRouteService.deleteById(id, username);
    }
}
