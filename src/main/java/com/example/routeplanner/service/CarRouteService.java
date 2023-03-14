package com.example.routeplanner.service;

import com.example.routeplanner.model.Route;
import com.example.routeplanner.model.Step;
import com.example.routeplanner.model.api.ApiResponse;
import com.example.routeplanner.model.api.ApiRoute;
import com.example.routeplanner.model.api.Leg;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CarRouteService {
    @Value("${api.url}")
    private String url;
    @Value("${api.key}")
    private String key;
    RestTemplate restTemplate = new RestTemplate();
    public List<Route> getRoutes(String destination, String origin){           // url  request
        URI uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("key", key)
                .queryParam("destination", destination)
                .queryParam("origin", origin)
                .queryParam("alternatives","true")
                .queryParam("departure_time", "now")
                .build()
                .toUri();
        ApiResponse response = restTemplate.getForObject(uri, ApiResponse.class);
        List<ApiRoute> routes = response.getRoutes();
        uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("key", key)
                .queryParam("destination", origin)
                .queryParam("origin", destination)
                .queryParam("alternatives","true")
                .queryParam("departure_time", "now")
                .build()
                .toUri();
        response = restTemplate.getForObject(uri, ApiResponse.class);
        routes.addAll(response.getRoutes());
        return routes.stream()
                .map(this::toRoute)
                .toList();
    }
    private Route toRoute(ApiRoute apiRoute){
        Route route = new Route();

        Leg leg = apiRoute.getLegs().get(0);
        int value = Integer.parseInt(leg.getDuration().get("value"));                                                   //tid

        List<Step> steps= leg.getSteps().stream()
                .map(step ->new Step(step.getDistance().get("text"), clearHtml(step.getHtmlInstructions())))
                .toList();

        route.setSteps(steps);
        route.setDurationInTraffic(leg.getDurationInTraffic().get("text"));

        String arrivalTime = LocalTime.now().plusSeconds(value).format(DateTimeFormatter.ofPattern("HH:mm"));
        route.setArrivalTime(arrivalTime);
        return route;
    }


    private String clearHtml(String string){
//        return string.replace("<b>","").replace("</b>", ""));

        return string.replaceAll("<(.*?)>", "");
    }
}
