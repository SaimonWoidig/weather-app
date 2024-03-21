package cz.woidig.backend.controller;

import cz.woidig.backend.dto.ErrorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ControllerExceptionHandlerTest {
    private ControllerExceptionHandler controllerExceptionHandler;

    @BeforeEach
    void setUp() {
        controllerExceptionHandler = new ControllerExceptionHandler();
    }

    @Test
    void test_handleNotFound() {
        NoHandlerFoundException e = new NoHandlerFoundException("GET", "/nonexistant", HttpHeaders.EMPTY);

        ErrorDTO expected = new ErrorDTO(404, "Not found");
        ErrorDTO actual = controllerExceptionHandler.handleNoHandlerFound(e, null);

        assertEquals(expected.message(), actual.message());
    }

    @Test
    void test_handleMissingServletRequestParameter() {
        MissingServletRequestParameterException e = new MissingServletRequestParameterException("key", "value");

        ErrorDTO expected = new ErrorDTO(400, "Missing request parameter: key");
        ErrorDTO actual = controllerExceptionHandler.handleMissingServletRequestParameter(e, null);

        assertEquals(expected.message(), actual.message());
    }

    @Test
    void test_handleIllegalArgumentException() {
        IllegalArgumentException e = new IllegalArgumentException("Illegal argument");

        ErrorDTO expected = new ErrorDTO(400, "Illegal argument");
        ErrorDTO actual = controllerExceptionHandler.handleIllegalArgumentException(e, null);

        assertEquals(expected.message(), actual.message());
    }

    @Test
    void test_handleUnhandledException() {
        Exception e = new Exception("Unhandled exception");

        ErrorDTO expected = new ErrorDTO(500, "Internal server error");
        ErrorDTO actual = controllerExceptionHandler.handleUnhandledException(e, null);

        assertEquals(expected.message(), actual.message());
    }
}