package cz.woidig.backend.controller.geo;

import cz.woidig.backend.dto.geo.GeoDTO;
import cz.woidig.backend.exceptions.GeoException;
import cz.woidig.backend.service.geo.GeoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class GeoController {
    private final GeoService geoService;

    @GetMapping("/geo")
    public GeoDTO getGeo(@RequestParam String ip) throws GeoException {
        return geoService.getGeoByIp(ip);
    }
}
