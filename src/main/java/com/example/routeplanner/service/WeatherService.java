package com.example.routeplanner.service;


import com.example.routeplanner.model.api.CurrentWeather;
import com.example.routeplanner.model.Weather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.example.routeplanner.model.api.ApiWeatherResponse;

import java.net.URI;

@Service
public class WeatherService {
    @Value("${api.weather.url}")
    private String urlWeather;
    @Value("${api.weather.key}")
    private String keyWeather;

    RestTemplate restTemplate = new RestTemplate();
    public Weather getWeather(String lat, String lng){                    //dest.koordinater G request
        URI uri = UriComponentsBuilder.fromUriString(urlWeather)
                .queryParam("appid", keyWeather)
                .queryParam("lat", lat)
                .queryParam("lon", lng)
                .queryParam("units", "metric")
                .queryParam("exclude", "minutely,hourly,daily,alerts")
                .build()
                .toUri();
        ApiWeatherResponse response = restTemplate.getForObject(uri, ApiWeatherResponse.class);
        CurrentWeather currentWeather = response.getCurrent();
        return new Weather(currentWeather.getTemp(), currentWeather.getFeelsLike(),
               currentWeather.getWindSpeed(), currentWeather.getWeather().get(0).get("description"));
    }

}