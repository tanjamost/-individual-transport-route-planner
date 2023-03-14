package com.example.routeplanner.service;

import com.example.routeplanner.model.Route;
import com.example.routeplanner.model.Step;
import com.example.routeplanner.model.api.ApiResponse;
import com.example.routeplanner.model.api.ApiRoute;
import com.example.routeplanner.model.api.Leg;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalkCycleRouteService {
    private final WeatherService weatherService;
    @Value("${api.url}")
    private String url;
    @Value("${api.key}")
    private String key;
    RestTemplate restTemplate = new RestTemplate();
    public List<Route> getRoutes(String mode, String destination, String origin){
        URI uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("key", key)
                .queryParam("mode", mode)
                .queryParam("destination", destination)
                .queryParam("origin", origin)
                .build()
                .toUri();
        ApiResponse response = restTemplate.getForObject(uri, ApiResponse.class);
        List<ApiRoute> routes = response.getRoutes();
        uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("key", key)
                .queryParam("mode", mode)
                .queryParam("destination", origin)
                .queryParam("origin", destination)
                .build()
                .toUri();
        response = restTemplate.getForObject(uri, ApiResponse.class);                       //retur resa
        routes.addAll(response.getRoutes());
        return routes.stream()
                .map(this::toRoute)
                .toList();
    }
    private Route toRoute(ApiRoute apiRoute){

        Route route = new Route();

        Leg leg = apiRoute.getLegs().get(0);
        int value = Integer.parseInt(leg.getDuration().get("value"));
        List<Step> steps= leg.getSteps().stream()
                .map(step ->new Step(step.getDistance().get("text"), clearHtml(step.getHtmlInstructions())))
                .toList();

        route.setSteps(steps);
        route.setDuration(leg.getDuration().get("text"));
        String arrivalTime = LocalTime.now().plusSeconds(value).format(DateTimeFormatter.ofPattern("HH:mm"));
        route.setArrivalTime(arrivalTime);

        route.setWeather(weatherService.getWeather(leg.getEndLocation().get("lat"), leg.getEndLocation().get("lng")));
        return route;
    }


    private String clearHtml(String string){
        return string.replace("<b>","").replace("</b>", "");
    }
}
