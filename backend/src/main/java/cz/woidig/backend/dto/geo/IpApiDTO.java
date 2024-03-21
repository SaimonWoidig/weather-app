package cz.woidig.backend.dto.geo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IpApiDTO(
        @JsonProperty("lat") Float latitude, @JsonProperty("lon") Float longitude, String status, String message) {
}
