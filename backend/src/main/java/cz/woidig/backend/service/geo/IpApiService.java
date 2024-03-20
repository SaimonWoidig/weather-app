package cz.woidig.backend.service.geo;

import cz.woidig.backend.dto.geo.GeoDTO;
import cz.woidig.backend.dto.geo.IpApiDTO;
import cz.woidig.backend.exceptions.GeoException;
import lombok.AllArgsConstructor;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@AllArgsConstructor
public class IpApiService implements GeoService {
    private static final String API_PROTO = "http";
    private static final String API_HOST = "ip-api.com";
    private static final String API_PATH = "/json/";

    private final InetAddressValidator inetAddressValidator;

    @Override
    public GeoDTO getGeoByIp(String ip) throws GeoException {
        // validate IP address
        if (!inetAddressValidator.isValidInet4Address(ip)) {
            throw new GeoException("Invalid IP address");
        }

        // call IpApi API
        UriBuilder uriBuilder = UriComponentsBuilder.newInstance();
        RestTemplate restTemplate = new RestTemplate();
        URI uri = uriBuilder.scheme(API_PROTO).host(API_HOST).path(API_PATH).path(ip)
                .queryParam("fields", "status,message,lat,lon")
                .build();
        ResponseEntity<IpApiDTO> response = restTemplate.getForEntity(uri, IpApiDTO.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new GeoException("IpApi API call failed with status code " + response.getStatusCode());
        }
        if (response.getBody() == null) {
            throw new GeoException("IpApi API call failed because response body is null");
        }

        // get response and check status
        IpApiDTO ipApiDTO = response.getBody();
        if (!ipApiDTO.status().equals("success")) {
            throw new GeoException(ipApiDTO.message());
        }
        return new GeoDTO(ipApiDTO.latitude(), ipApiDTO.longitude());
    }
}
