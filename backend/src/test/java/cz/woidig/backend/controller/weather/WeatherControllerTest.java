package cz.woidig.backend.controller.weather;

import cz.woidig.backend.dto.weather.WeatherDTO;
import cz.woidig.backend.dto.weather.WeatherDaysDTO;
import cz.woidig.backend.exceptions.WeatherException;
import cz.woidig.backend.service.weather.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class WeatherControllerTest {
    @Mock
    WeatherService weatherService;

    WeatherController weatherController;

    @BeforeEach
    void setUp() {
        weatherController = new WeatherController(weatherService);
    }

    @Test
    void test_getCurrentWeather_success() {
        Mockito.doReturn(new WeatherDTO(0.0f, 0.0f, 1))
                .when(weatherService).getCurrentWeather(Mockito.anyFloat(), Mockito.anyFloat());
        assertDoesNotThrow(() -> weatherController.getCurrentWeather(0.0f, 0.0f));
    }

    @Test
    void test_getCurrentWeather_throws() {
        Mockito.doThrow(new WeatherException("Error"))
                .when(weatherService).getCurrentWeather(Mockito.anyFloat(), Mockito.anyFloat());
        assertThrows(WeatherException.class, () -> weatherController.getCurrentWeather(0.0f, 0.0f));
    }

    @Test
    void test_get7DayForecast_success() {
        float latitude = 0.0f;
        float longitude = 0.0f;
        WeatherDaysDTO weatherDaysDTO = new WeatherDaysDTO(new ArrayList<>(), new ArrayList<>());
        Mockito.when(weatherService.get7DayForecast(latitude, longitude)).thenReturn(weatherDaysDTO);
        assertDoesNotThrow(() -> weatherController.get7DayForecast(latitude, longitude));
    }

    @Test
    void test_get7DayForecast_throws() {
        Mockito.doThrow(new WeatherException("Error"))
                .when(weatherService).get7DayForecast(Mockito.anyFloat(), Mockito.anyFloat());
        assertThrows(WeatherException.class, () -> weatherController.get7DayForecast(0.0f, 0.0f));
    }

    @Test
    void test_get7PreviousDays_success() {
        float latitude = 0.0f;
        float longitude = 0.0f;
        WeatherDaysDTO weatherDaysDTO = new WeatherDaysDTO(new ArrayList<>(), new ArrayList<>());
        Mockito.when(weatherService.get7PreviousDays(latitude, longitude)).thenReturn(weatherDaysDTO);
        assertDoesNotThrow(() -> weatherController.get7PreviousDays(latitude, longitude));
    }

    @Test
    void test_get7PreviousDays_throws() {
        Mockito.doThrow(new WeatherException("Error"))
                .when(weatherService).get7PreviousDays(Mockito.anyFloat(), Mockito.anyFloat());
        assertThrows(WeatherException.class, () -> weatherController.get7PreviousDays(0.0f, 0.0f));
    }
}