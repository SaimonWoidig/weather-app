package cz.woidig.backend.service.geo;

import cz.woidig.backend.dto.geo.GeoDTO;
import cz.woidig.backend.dto.geo.IpApiDTO;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class IpApiServiceTest {
    @Mock
    private RestTemplateBuilder mockRestTemplateBuilder;
    @Mock
    private RestTemplate mockRestTemplate;
    @Mock
    private InetAddressValidator mockInetAddressValidator;

    private IpApiService ipApiService;

    @BeforeEach
    void setUp() {
        ipApiService = new IpApiService(mockRestTemplateBuilder, mockInetAddressValidator);
    }

    @Test
    void getGeoByIp() {
        String ip = "1.1.1.1";

        Mockito.doReturn(true).when(mockInetAddressValidator).isValidInet4Address(Mockito.anyString());

        float latitude = 0.0f;
        float longitude = 0.0f;
        IpApiDTO requestData = new IpApiDTO(latitude, longitude, "success", null);
        ResponseEntity<IpApiDTO> responseEntity = ResponseEntity.ofNullable(requestData);
        Mockito.doReturn(responseEntity)
                .when(mockRestTemplate).getForEntity(Mockito.any(URI.class), Mockito.eq(IpApiDTO.class));
        Mockito.doReturn(mockRestTemplate)
                .when(mockRestTemplateBuilder).build();

        GeoDTO expected = new GeoDTO(latitude, longitude);

        GeoDTO actual = ipApiService.getGeoByIp(ip);

        assertEquals(expected, actual);
    }
}