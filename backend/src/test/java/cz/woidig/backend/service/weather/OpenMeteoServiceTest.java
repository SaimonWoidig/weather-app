package cz.woidig.backend.service.weather;

import cz.woidig.backend.dto.weather.WeatherDTO;
import cz.woidig.backend.dto.weather.WeatherDaysDTO;
import cz.woidig.backend.dto.weather.openmeteo.OpenMeteoCurrent;
import cz.woidig.backend.dto.weather.openmeteo.OpenMeteoDTO;
import cz.woidig.backend.dto.weather.openmeteo.OpenMeteoDaily;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
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
        OpenMeteoDTO requestData = new OpenMeteoDTO(meteoCurrent, null);

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

        OpenMeteoDTO responseData = new OpenMeteoDTO(null, null);
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

        OpenMeteoDTO responseData = new OpenMeteoDTO(meteoCurrent, null);
        ResponseEntity<OpenMeteoDTO> responseEntity = ResponseEntity.ofNullable(responseData);
        Mockito.doReturn(responseEntity)
                .when(mockRestTemplate).getForEntity(Mockito.any(URI.class), Mockito.eq(OpenMeteoDTO.class));
        Mockito.doReturn(mockRestTemplate)
                .when(mockRestTemplateBuilder).build();

        WeatherException expected = new WeatherException(expectedMessage);
        WeatherException actual = assertThrows(WeatherException.class, () -> openMeteoService.getCurrentWeather(latitude, longitude));
        assertEquals(expected.getMessage(), actual.getMessage());
    }

    @Test
    public void test_get7DayForecast_success() {
        float latitude = 0.0f;
        float longitude = 0.0f;
        int weatherCode = 1;

        List<WeatherDTO> expectedWeathers = new ArrayList<>();
        List<LocalDate> expectedDates = new ArrayList<>();
        String[] dateStrings = {"2024-01-01", "2024-01-02", "2024-01-03", "2024-01-04", "2024-01-05", "2024-01-06", "2024-01-07", "2024-01-08"};
        Float[] temperatureMaxs = {0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f};
        Float[] temperatureMins = {0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f};
        Float[] precipitations = {0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f};
        Integer[] weatherCodes = {weatherCode, weatherCode, weatherCode, weatherCode, weatherCode, weatherCode, weatherCode, weatherCode};

        for (int i = 0; i < 7; i++) {
            expectedWeathers.add(new WeatherDTO(temperatureMaxs[i + 1], temperatureMins[i + 1], weatherCodes[i + 1]));
            expectedDates.add(LocalDate.parse(dateStrings[i + 1]));
        }

        OpenMeteoDaily meteoDaily = new OpenMeteoDaily(
                dateStrings,
                temperatureMaxs,
                temperatureMins,
                precipitations,
                weatherCodes
        );

        OpenMeteoDTO requestData = new OpenMeteoDTO(null, meteoDaily);
        ResponseEntity<OpenMeteoDTO> responseEntity = ResponseEntity.ofNullable(requestData);
        Mockito.doReturn(responseEntity)
                .when(mockRestTemplate).getForEntity(Mockito.any(URI.class), Mockito.eq(OpenMeteoDTO.class));
        Mockito.doReturn(mockRestTemplate)
                .when(mockRestTemplateBuilder).build();


        WeatherDaysDTO expected = new WeatherDaysDTO(expectedWeathers, expectedDates);
        WeatherDaysDTO actual = openMeteoService.get7DayForecast(latitude, longitude);

        assertEquals(expected, actual);
    }

    @Test
    public void test_getPrevious7DayForecast_success() {
        float latitude = 0.0f;
        float longitude = 0.0f;
        int weatherCode = 1;

        List<WeatherDTO> expectedWeathers = new ArrayList<>();
        List<LocalDate> expectedDates = new ArrayList<>();
        String[] dateStrings = {"2024-01-01", "2024-01-02", "2024-01-03", "2024-01-04", "2024-01-05", "2024-01-06", "2024-01-07"};
        Float[] temperatureMaxs = {0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f};
        Float[] temperatureMins = {0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f};
        Float[] precipitations = {0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f};
        Integer[] weatherCodes = {weatherCode, weatherCode, weatherCode, weatherCode, weatherCode, weatherCode, weatherCode};

        for (int i = dateStrings.length - 1; i >= 0; i--) {
            expectedWeathers.add(new WeatherDTO(temperatureMaxs[i], temperatureMins[i], weatherCodes[i]));
            expectedDates.add(LocalDate.parse(dateStrings[i]));
        }

        OpenMeteoDaily meteoDaily = new OpenMeteoDaily(
                dateStrings,
                temperatureMaxs,
                temperatureMins,
                precipitations,
                weatherCodes
        );

        OpenMeteoDTO requestData = new OpenMeteoDTO(null, meteoDaily);
        ResponseEntity<OpenMeteoDTO> responseEntity = ResponseEntity.ofNullable(requestData);
        Mockito.doReturn(responseEntity)
                .when(mockRestTemplate).getForEntity(Mockito.any(URI.class), Mockito.eq(OpenMeteoDTO.class));
        Mockito.doReturn(mockRestTemplate)
                .when(mockRestTemplateBuilder).build();


        WeatherDaysDTO expected = new WeatherDaysDTO(expectedWeathers, expectedDates);
        WeatherDaysDTO actual = openMeteoService.get7PreviousDays(latitude, longitude);

        assertEquals(expected, actual);
    }

    @Test
    public void test_validateDailyData_success() {
        OpenMeteoDaily data = new OpenMeteoDaily(
                new String[]{"2023-01-01", "2023-01-02", "2023-01-03", "2023-01-04", "2023-01-05", "2023-01-06", "2023-01-07"},
                new Float[]{0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f},
                new Float[]{0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f},
                new Float[]{0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f},
                new Integer[]{1, 1, 1, 1, 1, 1, 1}
        );

        assertDoesNotThrow(() -> OpenMeteoService.validateDailyData(data));
    }

    @Test
    public void test_validateDailyData_nullDailyData() {
        assertThrows(WeatherException.class, () -> OpenMeteoService.validateDailyData(null));
    }


    private static Stream<Arguments> invalidDailyWeatherDataProvider() {
        String[] validDateStrings = new String[]{"2024-01-01"};
        Float[] validTemperaturesMax = new Float[]{0.0f};
        Float[] validTemperaturesMin = new Float[]{0.0f};
        Float[] validPrecipitations = new Float[]{0.0f};
        Integer[] validWeatherCodes = new Integer[]{1};

        return Stream.of(
                arguments(
                        new OpenMeteoDaily(null, null, null, null, null),
                        "OpenMeteo API call failed because date strings are null"
                ),
                arguments(
                        new OpenMeteoDaily(validDateStrings, null, null, null, null),
                        "OpenMeteo API call failed because temperatures max are null"
                ),
                arguments(
                        new OpenMeteoDaily(validDateStrings, validTemperaturesMax, null, null, null),
                        "OpenMeteo API call failed because temperatures min are null"
                ),
                arguments(
                        new OpenMeteoDaily(validDateStrings, validTemperaturesMax, validTemperaturesMin, null, null),
                        "OpenMeteo API call failed because precipitations are null"
                ),
                arguments(
                        new OpenMeteoDaily(validDateStrings, validTemperaturesMax, validTemperaturesMin, validPrecipitations, null),
                        "OpenMeteo API call failed because weather codes are null"
                ),
                arguments(
                        new OpenMeteoDaily(validDateStrings, new Float[]{}, validTemperaturesMin, validPrecipitations, validWeatherCodes),
                        "OpenMeteo API call failed because date strings and temperatures max have different lengths"
                ),
                arguments(
                        new OpenMeteoDaily(validDateStrings, validTemperaturesMax, new Float[]{}, validPrecipitations, validWeatherCodes),
                        "OpenMeteo API call failed because date strings and temperatures min have different lengths"
                ),
                arguments(
                        new OpenMeteoDaily(validDateStrings, validTemperaturesMax, validTemperaturesMin, new Float[]{}, validWeatherCodes),
                        "OpenMeteo API call failed because date strings and precipitations have different lengths"
                ),
                arguments(
                        new OpenMeteoDaily(validDateStrings, validTemperaturesMax, validTemperaturesMin, validPrecipitations, new Integer[]{}),
                        "OpenMeteo API call failed because date strings and weather codes have different lengths"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("invalidDailyWeatherDataProvider")
    public void test_validateDailyData_invalidDailyData(OpenMeteoDaily data, String expectedMessage) {
        WeatherException expected = new WeatherException(expectedMessage);
        WeatherException actual = assertThrows(WeatherException.class, () -> OpenMeteoService.validateDailyData(data));
        assertEquals(expected.getMessage(), actual.getMessage());
    }

    @Test
    public void test_validateCoords_success() {
        assertDoesNotThrow(() -> OpenMeteoService.validateCoords(0.0f, 0.0f));
    }

    @Test
    public void test_validateCoords_invalidLatitude() {
        assertThrows(IllegalArgumentException.class, () -> OpenMeteoService.validateCoords(-91.0f, 0.0f));
    }

    @Test
    public void test_validateCoords_invalidLongitude() {
        assertThrows(IllegalArgumentException.class, () -> OpenMeteoService.validateCoords(0.0f, 181.0f));
    }
}