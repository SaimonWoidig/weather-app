package cz.woidig.backend.controller.weather;

import cz.woidig.backend.dto.weather.WeatherDTO;
import cz.woidig.backend.exceptions.WeatherException;
import cz.woidig.backend.service.weather.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
}