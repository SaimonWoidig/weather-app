package cz.woidig.backend.service.geo;

import cz.woidig.backend.dto.geo.GeoDTO;
import cz.woidig.backend.dto.geo.IpApiDTO;
import cz.woidig.backend.exceptions.GeoException;
import lombok.AllArgsConstructor;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@AllArgsConstructor
public class IpApiService extends ConfigFallbackGeoService implements GeoService {
    private static final String API_PROTO = "http";
    private static final String API_HOST = "ip-api.com";
    private static final String API_PATH = "/json/";

    private final RestTemplateBuilder restTemplateBuilder;
    private final InetAddressValidator inetAddressValidator;

    @Override
    public GeoDTO getGeoByIp(String ip) throws GeoException {
        // validate IP address
        if (!inetAddressValidator.isValid(ip)) {
            throw new IllegalArgumentException("Invalid IP address " + ip);
        }

        // call IpApi API
        UriBuilder uriBuilder = UriComponentsBuilder.newInstance();
        RestTemplate restTemplate = restTemplateBuilder.build();
        URI uri = uriBuilder.scheme(API_PROTO).host(API_HOST).path(API_PATH).path(ip)
                .queryParam("fields", "status,message,lat,lon")
                .build();

        ResponseEntity<IpApiDTO> response;
        try {
            response = restTemplate.getForEntity(uri, IpApiDTO.class);
        } catch (RestClientException e) {
            throw new GeoException("IpApi API call failed", e);
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new GeoException("IpApi API call failed with status code " + response.getStatusCode().value());
        }
        if (response.getBody() == null) {
            throw new GeoException("IpApi API call failed because response body is null");
        }

        // get response and validate
        IpApiDTO ipApiDTO = response.getBody();
        validateIpApiDTO(ipApiDTO);

        return new GeoDTO(ipApiDTO.latitude(), ipApiDTO.longitude());
    }

    private static void validateIpApiDTO(IpApiDTO dto) throws IllegalArgumentException,GeoException {
        if (!dto.status().equals("success")) {
            if (dto.message() == null) {
                throw new GeoException("IpApi API call unsuccessful without message");
            }
            if (dto.message().equals("private range")) {
                throw new IllegalArgumentException("IP address in private range");
            }
            if (dto.message().equals("reserved range")) {
                throw new IllegalArgumentException("IP address in reserved range");
            }
            throw new GeoException(dto.message());
        }
    }
}
