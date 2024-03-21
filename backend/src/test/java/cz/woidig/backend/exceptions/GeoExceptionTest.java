package cz.woidig.backend.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeoExceptionTest {
    @Test
    void testGeoExceptionConstructorWithMessage() {
        String message = "Test message";
        GeoException geoException = new GeoException(message);
        assertEquals(message, geoException.getMessage());
    }

    @Test
    void testGeoExceptionConstructorWithMessageAndThrowable() {
        String message = "Test message";
        Throwable cause = new Throwable("Cause of the exception");
        GeoException geoException = new GeoException(message, cause);
        assertEquals(message, geoException.getMessage());
        assertEquals(cause, geoException.getCause());
    }
}