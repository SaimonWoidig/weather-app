package cz.woidig.backend.dto.weather.openmeteo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OpenMeteoDaily(
        @JsonProperty("time")
        String[] dateStrings,
        @JsonProperty("temperature_2m_max")
        Float[] temperaturesMax,
        @JsonProperty("temperature_2m_min")
        Float[] temperaturesMin,
        @JsonProperty("precipitation_sum")
        Float[] precipitations,
        @JsonProperty("weather_code")
        Integer[] weatherCodes
) {
}
