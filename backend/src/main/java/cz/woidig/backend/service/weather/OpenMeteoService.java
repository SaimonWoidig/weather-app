package cz.woidig.backend.service.weather;

import cz.woidig.backend.dto.weather.WeatherDTO;
import cz.woidig.backend.dto.weather.WeatherDaysDTO;
import cz.woidig.backend.dto.weather.openmeteo.OpenMeteoCurrent;
import cz.woidig.backend.dto.weather.openmeteo.OpenMeteoDTO;
import cz.woidig.backend.dto.weather.openmeteo.OpenMeteoDaily;
import cz.woidig.backend.exceptions.WeatherException;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private static final String VAR_TEMPERATURE_MIN = "temperature_2m_min";
    private static final String VAR_TEMPERATURE_MAX = "temperature_2m_max";
    private static final String VAR_PRECIPITATION_SUM = "precipitation_sum";

    private final RestTemplateBuilder restTemplateBuilder;

    public WeatherDTO getCurrentWeather(float latitude, float longitude) throws WeatherException {
        // Validate input
        if (latitude < -90.0f || latitude > 90.0f) {
            throw new IllegalArgumentException("Latitude must be between -90.0 and 90.0");
        }
        if (longitude < -180.0f || longitude > 180.0f) {
            throw new IllegalArgumentException("Longitude must be between -180.0 and 180.0");
        }

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
            throw new WeatherException("OpenMeteo API call failed", e);
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new WeatherException("OpenMeteo API call failed with status code " + response.getStatusCode().value());
        }
        if (response.getBody() == null) {
            throw new WeatherException("OpenMeteo API call failed because response body is null");
        }

        OpenMeteoCurrent data = response.getBody().current();
        if (data == null) {
            throw new WeatherException("OpenMeteo API call failed because current data is null");
        }

        if (data.temperature() == null) {
            throw new WeatherException("OpenMeteo API call failed because temperature is null");
        }
        if (data.precipitation() == null) {
            throw new WeatherException("OpenMeteo API call failed because precipitation is null");
        }
        if (data.weatherCode() == null) {
            throw new WeatherException("OpenMeteo API call failed because weather code is null");
        }

        return new WeatherDTO(data.temperature(), data.precipitation(), data.weatherCode());
    }

    public WeatherDaysDTO get7DayForecast(float latitude, float longitude) throws WeatherException {
        if (latitude < -90.0f || latitude > 90.0f) {
            throw new IllegalArgumentException("Latitude must be between -90.0 and 90.0");
        }
        if (longitude < -180.0f || longitude > 180.0f) {
            throw new IllegalArgumentException("Longitude must be between -180.0 and 180.0");
        }

        // Call OpenMeteo API
        UriBuilder uriBuilder = UriComponentsBuilder.newInstance();
        RestTemplate restTemplate = restTemplateBuilder.build();
        URI uri = uriBuilder.scheme(API_PROTO).host(API_HOST).path(API_PATH)
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .queryParam("daily", String.join(
                        ",",
                        VAR_TEMPERATURE_MIN,
                        VAR_TEMPERATURE_MAX,
                        VAR_PRECIPITATION_SUM,
                        VAR_WEATHER
                ))
                .queryParam("forecast_days", "8")// today + 7 days
                .queryParam("timezone", "UTC")
                .build();

        ResponseEntity<OpenMeteoDTO> response;
        try {
            response = restTemplate.getForEntity(uri, OpenMeteoDTO.class);
        } catch (RestClientException e) {
            throw new WeatherException("OpenMeteo API call failed", e);
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new WeatherException("OpenMeteo API call failed with status code " + response.getStatusCode().value());
        }
        if (response.getBody() == null) {
            throw new WeatherException("OpenMeteo API call failed because response body is null");
        }

        OpenMeteoDaily data = response.getBody().daily();
        validateDailyData(data);

        List<WeatherDTO> weatherDTOList = new ArrayList<>();
        List<LocalDate> days = new ArrayList<>();

        // omit today
        for (int i = 1; i < data.dateStrings().length; i++) {
            float avgTemperature = (data.temperaturesMax()[i] + data.temperaturesMin()[i]) / 2.0f;
            weatherDTOList.add(new WeatherDTO(avgTemperature, data.precipitations()[i], data.weatherCodes()[i]));
            days.add(LocalDate.parse(data.dateStrings()[i]));
        }

        return new WeatherDaysDTO(weatherDTOList, days);
    }

    public WeatherDaysDTO get7PreviousDays(float latitude, float longitude) throws WeatherException {
        if (latitude < -90.0f || latitude > 90.0f) {
            throw new IllegalArgumentException("Latitude must be between -90.0 and 90.0");
        }
        if (longitude < -180.0f || longitude > 180.0f) {
            throw new IllegalArgumentException("Longitude must be between -180.0 and 180.0");
        }

        // Call OpenMeteo API
        UriBuilder uriBuilder = UriComponentsBuilder.newInstance();
        RestTemplate restTemplate = restTemplateBuilder.build();
        URI uri = uriBuilder.scheme(API_PROTO).host(API_HOST).path(API_PATH)
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .queryParam("daily", String.join(
                        ",",
                        VAR_TEMPERATURE_MIN,
                        VAR_TEMPERATURE_MAX,
                        VAR_PRECIPITATION_SUM,
                        VAR_WEATHER
                ))
                .queryParam("forecast_days", "0") // without forecast
                .queryParam("past_days", "7") // 7 days to past
                .queryParam("timezone", "UTC")
                .build();
        ResponseEntity<OpenMeteoDTO> response;
        try {
            response = restTemplate.getForEntity(uri, OpenMeteoDTO.class);
        } catch (RestClientException e) {
            throw new WeatherException("OpenMeteo API call failed", e);
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new WeatherException("OpenMeteo API call failed with status code " + response.getStatusCode().value());
        }
        if (response.getBody() == null) {
            throw new WeatherException("OpenMeteo API call failed because response body is null");
        }

        OpenMeteoDaily data = response.getBody().daily();
        validateDailyData(data);

        List<WeatherDTO> weatherDTOList = new ArrayList<>();
        List<LocalDate> days = new ArrayList<>();

        // iterate backwards to get from most recent to oldest
        for (int i = data.dateStrings().length - 1; i >= 0; i--) {
            float avgTemperature = (data.temperaturesMax()[i] + data.temperaturesMin()[i]) / 2.0f;
            weatherDTOList.add(new WeatherDTO(avgTemperature, data.precipitations()[i], data.weatherCodes()[i]));
            days.add(LocalDate.parse(data.dateStrings()[i]));
        }

        return new WeatherDaysDTO(weatherDTOList, days);
    }

    static void validateDailyData(OpenMeteoDaily data) {
        if (data == null) {
            throw new WeatherException("OpenMeteo API call failed because daily data is null");
        }
        if (data.dateStrings() == null) {
            throw new WeatherException("OpenMeteo API call failed because date strings are null");
        }
        if (data.temperaturesMax() == null) {
            throw new WeatherException("OpenMeteo API call failed because temperatures max are null");
        }
        if (data.temperaturesMin() == null) {
            throw new WeatherException("OpenMeteo API call failed because temperatures min are null");
        }
        if (data.precipitations() == null) {
            throw new WeatherException("OpenMeteo API call failed because precipitations are null");
        }
        if (data.weatherCodes() == null) {
            throw new WeatherException("OpenMeteo API call failed because weather codes are null");
        }
        if (data.dateStrings().length != data.temperaturesMax().length) {
            throw new WeatherException("OpenMeteo API call failed because date strings and temperatures max have different lengths");
        }
        if (data.dateStrings().length != data.temperaturesMin().length) {
            throw new WeatherException("OpenMeteo API call failed because date strings and temperatures min have different lengths");
        }
        if (data.dateStrings().length != data.precipitations().length) {
            throw new WeatherException("OpenMeteo API call failed because date strings and precipitations have different lengths");
        }
        if (data.dateStrings().length != data.weatherCodes().length) {
            throw new WeatherException("OpenMeteo API call failed because date strings and weather codes have different lengths");
        }
    }
}
