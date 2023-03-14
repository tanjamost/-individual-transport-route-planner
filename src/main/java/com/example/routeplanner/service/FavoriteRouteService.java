package com.example.routeplanner.service;

import com.example.routeplanner.model.FavoriteRoute;
import com.example.routeplanner.model.Route;
import com.example.routeplanner.model.Step;
import com.example.routeplanner.model.api.ApiResponse;
import com.example.routeplanner.model.api.ApiRoute;
import com.example.routeplanner.model.api.Leg;
import com.example.routeplanner.repository.FavoriteRouteRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class FavoriteRouteService {

    @Value("https://maps.googleapis.com/maps/api/directions/json")
    private String url;
    private final FavoriteRouteRepository favoriteRouteRepository;
    private final WeatherService weatherService;
    @Value("${api.key}")
    private String key;

    private RestTemplate restTemplate = new RestTemplate();

    public void saveRoute(String destination, String origin, String mode, String username){
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
                .queryParam("destination", destination)
                .queryParam("origin",origin)
                .queryParam("key", key);
        switch(mode) {
            case "walking" -> uriComponentsBuilder = uriComponentsBuilder.queryParam("mode","walking");
            case "cycling" -> uriComponentsBuilder = uriComponentsBuilder.queryParam("mode","bicycling");
            case "driving" -> uriComponentsBuilder = uriComponentsBuilder
                    .queryParam("departure_time", "now")
                    .queryParam("alternatives","true");
            default -> {                                        //om fel mode anges-gar ut
                return;
            }
        }
        URI uri = uriComponentsBuilder
                .build()
                .toUri();
        favoriteRouteRepository.save(new FavoriteRoute(null, username, uri.toString()));
    }

    public List<Route> getRoutesByUsername(String username){

        List<FavoriteRoute> favoriteRoutes = favoriteRouteRepository.findAllByUsername(username);


        List<ApiRoute> apiRoutes = new ArrayList<>();
        for (FavoriteRoute favoriteRoute : favoriteRoutes) {
            URI uri = UriComponentsBuilder.fromUriString(favoriteRoute.getUrl())
                    .queryParam("key", key)
                    .build()
                    .toUri();
            ApiResponse response = restTemplate.getForObject(uri, ApiResponse.class);
            apiRoutes.addAll(response.getRoutes().stream()
                    .peek( apiRoute -> apiRoute.setId(favoriteRoute.getId()))
                    .toList());
        }

        return apiRoutes.stream()
                .map(this::toRoute)
                .toList();
    }
    private Route toRoute(ApiRoute apiRoute){                                   //api-route to min route

        Route route = new Route();

        Leg leg = apiRoute.getLegs().get(0);
        int value = Integer.parseInt(leg.getDuration().get("value"));
        List<Step> steps= leg.getSteps().stream()
                .map(step ->new Step(step.getDistance().get("text"), clearHtml(step.getHtmlInstructions())))
                .toList();

        route.setSteps(steps);
        route.setDuration(leg.getDuration().get("text"));
        route.setId(apiRoute.getId());
        String arrivalTime = LocalTime.now().plusSeconds(value).format(DateTimeFormatter.ofPattern("HH:mm"));          //realTid+gångTid
        route.setArrivalTime(arrivalTime);

        route.setWeather(weatherService.getWeather(leg.getEndLocation().get("lat"), leg.getEndLocation().get("lng")));
        return route;
    }
    private String clearHtml(String string){

        return string.replace("<b>","").replace("</b>", "");
    }

    public void deleteById(int id, String username){
        Optional<FavoriteRoute> favoriteRoute = favoriteRouteRepository.findById(id);
        if (favoriteRoute.isPresent() && favoriteRoute.get().getUsername().equals(username)){
            favoriteRouteRepository.deleteById(id);
        }
    }
}
