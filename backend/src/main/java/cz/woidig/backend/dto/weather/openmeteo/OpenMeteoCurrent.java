package cz.woidig.backend.dto.weather.openmeteo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OpenMeteoCurrent(
        @JsonProperty("temperature_2m")
        Float temperature,
        @JsonProperty("precipitation")
        Float precipitation,
        @JsonProperty("weather_code")
        Integer weatherCode
) {
}
