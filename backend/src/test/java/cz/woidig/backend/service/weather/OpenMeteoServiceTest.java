package cz.woidig.backend.service.weather;

import cz.woidig.backend.dto.weather.WeatherDTO;
import cz.woidig.backend.dto.weather.openmeteo.OpenMeteoCurrent;
import cz.woidig.backend.dto.weather.openmeteo.OpenMeteoDTO;
import cz.woidig.backend.exceptions.WeatherException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

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
    public void test_GetCurrentWeather_success() {
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

    private static Stream<Arguments> invalidLatitudeLongitudeProvider() {
        return Stream.of(
                arguments(999f, 0.0f, "Latitude must be between -90.0 and 90.0"),
                arguments(-999f, 0.0f, "Latitude must be between -90.0 and 90.0"),
                arguments(0.0f, 999f, "Longitude must be between -180.0 and 180.0"),
                arguments(0.0f, -999f, "Longitude must be between -180.0 and 180.0")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidLatitudeLongitudeProvider")
    public void test_GetCurrentWeather_invalid_coordinates(float latitude, float longitude, String expectedMessage) {
        IllegalArgumentException expected = new IllegalArgumentException(expectedMessage);
        IllegalArgumentException actual = assertThrows(IllegalArgumentException.class, () -> openMeteoService.getCurrentWeather(latitude, longitude));
        assertEquals(expected.getMessage(), actual.getMessage());
    }

    @Test
    public void test_GetCurrentWeather_rest_exception() {
        float latitude = 0.0f;
        float longitude = 0.0f;

        Mockito.doThrow(RestClientException.class)
                .when(mockRestTemplate).getForEntity(Mockito.any(URI.class), Mockito.eq(OpenMeteoDTO.class));
        Mockito.doReturn(mockRestTemplate)
                .when(mockRestTemplateBuilder).build();

        WeatherException expected = new WeatherException("OpenMeteo API call failed");
        WeatherException actual = assertThrows(WeatherException.class, () -> openMeteoService.getCurrentWeather(latitude, longitude));
        assertEquals(expected.getMessage(), actual.getMessage());
    }

    @Test
    public void test_GetCurrentWeather_unsuccessful_response() {
        float latitude = 0.0f;
        float longitude = 0.0f;

        ResponseEntity<OpenMeteoDTO> responseEntity = ResponseEntity.internalServerError().body(null);
        Mockito.doReturn(responseEntity)
                .when(mockRestTemplate).getForEntity(Mockito.any(URI.class), Mockito.eq(OpenMeteoDTO.class));
        Mockito.doReturn(mockRestTemplate)
                .when(mockRestTemplateBuilder).build();

        WeatherException expected = new WeatherException("OpenMeteo API call failed with status code 500");
        WeatherException actual = assertThrows(WeatherException.class, () -> openMeteoService.getCurrentWeather(latitude, longitude));
        assertEquals(expected.getMessage(), actual.getMessage());
    }

    @Test
    public void test_GetCurrentWeather_null_response_body() {
        float latitude = 0.0f;
        float longitude = 0.0f;

        ResponseEntity<OpenMeteoDTO> responseEntity = ResponseEntity.ok(null);
        Mockito.doReturn(responseEntity)
                .when(mockRestTemplate).getForEntity(Mockito.any(URI.class), Mockito.eq(OpenMeteoDTO.class));
        Mockito.doReturn(mockRestTemplate)
                .when(mockRestTemplateBuilder).build();

        WeatherException expected = new WeatherException("OpenMeteo API call failed because response body is null");
        WeatherException actual = assertThrows(WeatherException.class, () -> openMeteoService.getCurrentWeather(latitude, longitude));
        assertEquals(expected.getMessage(), actual.getMessage());
    }

    @Test
    public void test_GetCurrentWeather_current_data_is_null() {
        float latitude = 0.0f;
        float longitude = 0.0f;

        OpenMeteoDTO responseData = new OpenMeteoDTO(null);
        ResponseEntity<OpenMeteoDTO> responseEntity = ResponseEntity.ofNullable(responseData);
        Mockito.doReturn(responseEntity)
                .when(mockRestTemplate).getForEntity(Mockito.any(URI.class), Mockito.eq(OpenMeteoDTO.class));
        Mockito.doReturn(mockRestTemplate)
                .when(mockRestTemplateBuilder).build();

        WeatherException expected = new WeatherException("OpenMeteo API call failed because current data is null");
        WeatherException actual = assertThrows(WeatherException.class, () -> openMeteoService.getCurrentWeather(latitude, longitude));
        assertEquals(expected.getMessage(), actual.getMessage());
    }

    private static Stream<Arguments> nullyWeatherDataProvider() {
        return Stream.of(
                arguments(
                        new OpenMeteoCurrent(null, 0.0f, 1),
                        "OpenMeteo API call failed because temperature is null"),
                arguments(
                        new OpenMeteoCurrent(1.0f, null, 1),
                        "OpenMeteo API call failed because precipitation is null"),
                arguments(
                        new OpenMeteoCurrent(1.0f, 0.0f, null),
                        "OpenMeteo API call failed because weather code is null")
        );
    }

    @ParameterizedTest
    @MethodSource("nullyWeatherDataProvider")
    public void test_GetCurrentWeather_weather_data_is_null(OpenMeteoCurrent meteoCurrent, String expectedMessage) {
        float latitude = 0.0f;
        float longitude = 0.0f;

        OpenMeteoDTO responseData = new OpenMeteoDTO(meteoCurrent);
        ResponseEntity<OpenMeteoDTO> responseEntity = ResponseEntity.ofNullable(responseData);
        Mockito.doReturn(responseEntity)
                .when(mockRestTemplate).getForEntity(Mockito.any(URI.class), Mockito.eq(OpenMeteoDTO.class));
        Mockito.doReturn(mockRestTemplate)
                .when(mockRestTemplateBuilder).build();

        WeatherException expected = new WeatherException(expectedMessage);
        WeatherException actual = assertThrows(WeatherException.class, () -> openMeteoService.getCurrentWeather(latitude, longitude));
        assertEquals(expected.getMessage(), actual.getMessage());
    }
}