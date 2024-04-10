package cz.woidig.backend.controller.geo;

import cz.woidig.backend.dto.geo.GeoDTO;
import cz.woidig.backend.exceptions.GeoException;
import cz.woidig.backend.service.geo.GeoService;
import cz.woidig.backend.service.geo.IpApiService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/geo")
public class GeoController {
    private final GeoService geoService;

    @GetMapping("/fallback")
    public GeoDTO getFallback() {
        return geoService.getFallback();
    }

    @GetMapping("/ip")
    public GeoDTO getGeoByIP(@RequestParam String ip) throws GeoException {
        return geoService.getGeoByIp(ip);
    }
}
