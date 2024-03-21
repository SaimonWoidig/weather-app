package cz.woidig.backend.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherExceptionTest {
    @Test
    void testWeatherApiExceptionConstructorWithMessage() {
        String message = "Test message";
        WeatherException weatherException = new WeatherException(message);
        assertEquals(message, weatherException.getMessage());
    }

    @Test
    void testWeatherApiExceptionConstructorWithMessageAndThrowable() {
        String message = "Test message";
        Throwable cause = new Throwable("Cause of the exception");
        WeatherException weatherException = new WeatherException(message, cause);
        assertEquals(message, weatherException.getMessage());
        assertEquals(cause, weatherException.getCause());
    }
}