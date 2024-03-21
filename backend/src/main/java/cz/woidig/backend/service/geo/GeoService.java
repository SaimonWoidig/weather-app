package cz.woidig.backend.service.geo;

import cz.woidig.backend.dto.geo.GeoDTO;
import cz.woidig.backend.exceptions.GeoException;

public interface GeoService {
    GeoDTO getGeoByIp(String ip) throws GeoException;
}
