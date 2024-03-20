package cz.woidig.backend.service.weather;

import cz.woidig.backend.dto.weather.WeatherDTO;
import cz.woidig.backend.dto.weather.openmeteo.OpenMeteoCurrent;
import cz.woidig.backend.dto.weather.openmeteo.OpenMeteoDTO;
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
class OpenMeteoServiceTest {
    @Mock
    private RestTemplateBuilder mockRestTemplateBuilder;
    @Mock
    private RestTemplate mockRestTemplate;

    private OpenMeteoService openMeteoService;

    @BeforeEach
    void setUp() {
        openMeteoService = new OpenMeteoService(mockRestTemplateBuilder);
    }

    @Test
    public void testGetCurrentWeather_successfulCall() {
        float latitude = 0.0f;
        float longitude = 0.0f;

        float temperature = 1.0f;
        float precipitation = 0.0f;
        int weatherCode = 1;
        OpenMeteoCurrent meteoCurrent = new OpenMeteoCurrent(temperature, precipitation, weatherCode);
        OpenMeteoDTO requestData = new OpenMeteoDTO(meteoCurrent);

        ResponseEntity<OpenMeteoDTO> responseEntity = ResponseEntity.ofNullable(requestData);
        Mockito.doReturn(responseEntity)
                .when(mockRestTemplate).getForEntity(Mockito.any(URI.class), Mockito.eq(OpenMeteoDTO.class));
        Mockito.doReturn(mockRestTemplate)
                .when(mockRestTemplateBuilder).build();

        WeatherDTO expected = new WeatherDTO(temperature, precipitation, weatherCode);

        WeatherDTO actual = openMeteoService.getCurrentWeather(latitude, longitude);

        assertEquals(expected, actual);
    }
}