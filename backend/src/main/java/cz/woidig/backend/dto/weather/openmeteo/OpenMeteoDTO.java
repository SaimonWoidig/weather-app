package cz.woidig.backend.dto.weather.openmeteo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OpenMeteoDTO(
        @JsonProperty("current")
        OpenMeteoCurrent current
) {
}
