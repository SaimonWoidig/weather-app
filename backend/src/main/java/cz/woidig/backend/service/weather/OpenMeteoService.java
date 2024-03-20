package cz.woidig.backend.service.weather;

import com.openmeteo.sdk.*;
import cz.woidig.backend.dto.weather.WeatherDTO;
import cz.woidig.backend.exceptions.WeatherApiException;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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

    public WeatherDTO getCurrentWeather(float latitude, float longitude) throws WeatherApiException {
        // Call OpenMeteo API
        UriBuilder uriBuilder = UriComponentsBuilder.newInstance();
        URI uri = uriBuilder.scheme(API_PROTO).host(API_HOST).path(API_PATH)
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .queryParam("current", String.join(",", VAR_TEMPERATURE, VAR_PRECIPITATION, VAR_WEATHER))
                .queryParam("format", "flatbuffers")
                .build();
        ResponseEntity<byte[]> response = new RestTemplate().getForEntity(uri, byte[].class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new WeatherApiException("OpenMeteo API call failed with status code " + response.getStatusCode());
        }
        if (response.getBody() == null) {
            throw new WeatherApiException("OpenMeteo API call failed because response body is null");
        }

        // Get response from flatbuffer
        ByteBuffer byteBuffer = ByteBuffer.wrap(response.getBody()).order(ByteOrder.LITTLE_ENDIAN);
        WeatherApiResponse weatherApiResponse = WeatherApiResponse.getRootAsWeatherApiResponse(byteBuffer.position(4));
        VariablesWithTime current = weatherApiResponse.current();

        // Extract variables from response
        VariableWithValues temperature = new VariablesSearch(current).variable(Variable.temperature).first();
        VariableWithValues precipitation = new VariablesSearch(current).variable(Variable.precipitation).first();
        VariableWithValues weatherCode = new VariablesSearch(current).variable(Variable.weather_code).first();
        if (temperature == null || precipitation == null || weatherCode == null) {
            throw new WeatherApiException("OpenMeteo API call failed because temperature, precipitation or weatherCode is null");
        }
        return new WeatherDTO(temperature.value(), precipitation.value(), (int) weatherCode.value());
    }

}
