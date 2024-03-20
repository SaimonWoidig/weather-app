package cz.woidig.backend.service.weather;

import cz.woidig.backend.dto.weather.WeatherDTO;
import cz.woidig.backend.dto.weather.openmeteo.OpenMeteoCurrent;
import cz.woidig.backend.dto.weather.openmeteo.OpenMeteoDTO;
import cz.woidig.backend.exceptions.WeatherApiException;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@AllArgsConstructor
@Log
public class OpenMeteoService implements WeatherService {
    private static final String API_PROTO = "https";
    private static final String API_HOST = "api.open-meteo.com";
    private static final String API_PATH = "/v1/forecast";

    private static final String VAR_TEMPERATURE = "temperature_2m";
    private static final String VAR_PRECIPITATION = "precipitation";
    private static final String VAR_WEATHER = "weather_code";

    private final RestTemplateBuilder restTemplateBuilder;

    public WeatherDTO getCurrentWeather(float latitude, float longitude) throws WeatherApiException {
        // Call OpenMeteo API
        UriBuilder uriBuilder = UriComponentsBuilder.newInstance();
        RestTemplate restTemplate = restTemplateBuilder.build();
        URI uri = uriBuilder.scheme(API_PROTO).host(API_HOST).path(API_PATH)
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .queryParam("current", String.join(",", VAR_TEMPERATURE, VAR_PRECIPITATION, VAR_WEATHER))
                .build();
        ResponseEntity<OpenMeteoDTO> response;
        try {
            response = restTemplate.getForEntity(uri, OpenMeteoDTO.class);
        } catch (RestClientException e) {
            throw new WeatherApiException("OpenMeteo API call failed", e);
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new WeatherApiException("OpenMeteo API call failed with status code " + response.getStatusCode());
        }
        if (response.getBody() == null) {
            throw new WeatherApiException("OpenMeteo API call failed because response body is null");
        }

        OpenMeteoCurrent data = response.getBody().current();
        if (data == null) {
            throw new WeatherApiException("OpenMeteo API call failed because current data is null");
        }
        
        if (data.temperature() == null) {
            throw new WeatherApiException("OpenMeteo API call failed because temperature is null");
        }
        if (data.precipitation() == null) {
            throw new WeatherApiException("OpenMeteo API call failed because precipitation is null");
        }
        if (data.weatherCode() == null) {
            throw new WeatherApiException("OpenMeteo API call failed because weather code is null");
        }

        return new WeatherDTO(data.temperature(), data.precipitation(), data.weatherCode());
    }

}
