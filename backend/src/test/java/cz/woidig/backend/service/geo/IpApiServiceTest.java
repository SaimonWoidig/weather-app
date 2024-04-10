package cz.woidig.backend.service.geo;

import cz.woidig.backend.dto.geo.GeoDTO;
import cz.woidig.backend.dto.geo.IpApiDTO;
import cz.woidig.backend.exceptions.GeoException;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class IpApiServiceTest {
    @Mock
    private RestTemplateBuilder mockRestTemplateBuilder;
    @Mock
    private RestTemplate mockRestTemplate;

    private IpApiService ipApiService;

    @BeforeEach
    void setUp() {
        InetAddressValidator inetAddressValidator = InetAddressValidator.getInstance();
        ipApiService = new IpApiService(mockRestTemplateBuilder, inetAddressValidator);
    }

    @Test
    void test_getGeoByIp_success() {
        String ip = "1.1.1.1";

        float latitude = 0.0f;
        float longitude = 0.0f;
        IpApiDTO responseData = new IpApiDTO(latitude, longitude, "success", null);
        ResponseEntity<IpApiDTO> responseEntity = ResponseEntity.ofNullable(responseData);
        Mockito.doReturn(responseEntity)
                .when(mockRestTemplate).getForEntity(Mockito.any(URI.class), Mockito.eq(IpApiDTO.class));
        Mockito.doReturn(mockRestTemplate)
                .when(mockRestTemplateBuilder).build();

        GeoDTO expected = new GeoDTO(latitude, longitude);

        GeoDTO actual = ipApiService.getGeoByIp(ip);

        assertEquals(expected, actual);
    }

    @Test
    void test_getGeoByIp_invalid_ip() {
        String ip = "not_a_valid_ip";

        assertThrows(IllegalArgumentException.class, () -> ipApiService.getGeoByIp(ip));
    }

    @Test
    void test_getGeoByIp_rest_exception() {
        String ip = "1.1.1.1";

        Mockito.doThrow(RestClientException.class)
                .when(mockRestTemplate).getForEntity(Mockito.any(URI.class), Mockito.eq(IpApiDTO.class));
        Mockito.doReturn(mockRestTemplate)
                .when(mockRestTemplateBuilder).build();

        GeoException expected = new GeoException("IpApi API call failed");
        GeoException actual = assertThrows(GeoException.class, () -> ipApiService.getGeoByIp(ip));
        assertEquals(expected.getMessage(), actual.getMessage());
    }

    @Test
    void test_getGeoByIp_unsuccessful_response() {
        String ip = "1.1.1.1";

        ResponseEntity<IpApiDTO> responseEntity = ResponseEntity.internalServerError().body(null);
        Mockito.doReturn(responseEntity)
                .when(mockRestTemplate).getForEntity(Mockito.any(URI.class), Mockito.eq(IpApiDTO.class));
        Mockito.doReturn(mockRestTemplate)
                .when(mockRestTemplateBuilder).build();

        GeoException expected = new GeoException("IpApi API call failed with status code 500");
        GeoException actual = assertThrows(GeoException.class, () -> ipApiService.getGeoByIp(ip));
        assertEquals(expected.getMessage(), actual.getMessage());
    }

    @Test
    void test_getGeoByIp_null_response_body() {
        String ip = "1.1.1.1";

        ResponseEntity<IpApiDTO> responseEntity = ResponseEntity.ok(null);
        Mockito.doReturn(responseEntity)
                .when(mockRestTemplate).getForEntity(Mockito.any(URI.class), Mockito.eq(IpApiDTO.class));
        Mockito.doReturn(mockRestTemplate)
                .when(mockRestTemplateBuilder).build();

        GeoException expected = new GeoException("IpApi API call failed because response body is null");
        GeoException actual = assertThrows(GeoException.class, () -> ipApiService.getGeoByIp(ip));
        assertEquals(expected.getMessage(), actual.getMessage());
    }

    @Test
    void test_getGeoByIp_status_fail() {
        String ip = "1.1.1.1";

        String failMessage = "test message";
        IpApiDTO responseData = new IpApiDTO(0.0f, 0.0f, "fail", failMessage);
        ResponseEntity<IpApiDTO> responseEntity = ResponseEntity.ofNullable(responseData);
        Mockito.doReturn(responseEntity)
                .when(mockRestTemplate).getForEntity(Mockito.any(URI.class), Mockito.eq(IpApiDTO.class));
        Mockito.doReturn(mockRestTemplate)
                .when(mockRestTemplateBuilder).build();

        GeoException expected = new GeoException(failMessage);
        GeoException actual = assertThrows(GeoException.class, () -> ipApiService.getGeoByIp(ip));
        assertEquals(expected.getMessage(), actual.getMessage());
    }
}