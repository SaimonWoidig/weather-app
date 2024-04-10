package cz.woidig.backend.service.geo;

import cz.woidig.backend.dto.geo.GeoDTO;
import org.springframework.beans.factory.annotation.Value;

public abstract class ConfigFallbackGeoService implements GeoService {
    @Value("${backend.geo.fallback.latitude}")
    private float fallbackLatitude;
    @Value("${backend.geo.fallback.longitude}")
    private float fallbackLongitude;

    private GeoDTO fallbackGeo;

    @Override
    public GeoDTO getFallback() {
        if (fallbackGeo == null) {
            fallbackGeo = new GeoDTO(fallbackLatitude, fallbackLongitude);
        }
        return fallbackGeo;
    }
}
