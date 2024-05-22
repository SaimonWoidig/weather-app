package cz.woidig.backend.controller;

import cz.woidig.backend.dto.ErrorDTO;
import cz.woidig.backend.exceptions.InvalidPasswordException;
import cz.woidig.backend.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
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

    @Test
    void test_handleUserAlreadyExistsException() {
        UserAlreadyExistsException e = new UserAlreadyExistsException("User already exists");

        ErrorDTO expected = new ErrorDTO(409, "User already exists");
        ErrorDTO actual = controllerExceptionHandler.handleUserAlreadyExistsException(e, null);

        assertEquals(expected.message(), actual.message());
    }

    @Test
    void test_handleUserNotFoundException() {
        UsernameNotFoundException e = new UsernameNotFoundException("User not found");

        ErrorDTO expected = new ErrorDTO(401, "User not found");
        ErrorDTO actual = controllerExceptionHandler.handleUserNotFoundException(e, null);

        assertEquals(expected.message(), actual.message());
    }

    @Test
    void test_handleInvalidPasswordException() {
        InvalidPasswordException e = new InvalidPasswordException("Invalid password");

        ErrorDTO expected = new ErrorDTO(401, "Invalid password");
        ErrorDTO actual = controllerExceptionHandler.handleInvalidPasswordException(e, null);

        assertEquals(expected.message(), actual.message());
    }

    @Test
    void test_handleHttpRequestMethodNotSupportedException() {
        HttpRequestMethodNotSupportedException e = new HttpRequestMethodNotSupportedException("TEST");

        ErrorDTO expected = new ErrorDTO(405, "Request method 'TEST' is not supported");
        ErrorDTO actual = controllerExceptionHandler.handleHttpRequestMethodNotSupportedException(e, null);

        assertEquals(expected.message(), actual.message());
    }

    @Test
    void test_handleHttpMediaTypeNotSupportedException() {
        HttpMediaTypeNotSupportedException e = new HttpMediaTypeNotSupportedException("Content-Type 'application/test123' is not supported");

        ErrorDTO expected = new ErrorDTO(415, "Content-Type 'application/test123' is not supported");
        ErrorDTO actual = controllerExceptionHandler.handleHttpMediaTypeNotSupportedException(e, null);

        assertEquals(expected.message(), actual.message());
    }

    @Test
    void test_handleMethodArgumentTypeMismatchException() {
        MethodParameter methodParameter = Mockito.mock(MethodParameter.class);
        Mockito.when(methodParameter.getParameterName()).thenReturn("latitude");
        MethodArgumentTypeMismatchException e = new MethodArgumentTypeMismatchException(
                "invalid value", Float.class, "latitude", methodParameter, null
        );

        ErrorDTO expected = new ErrorDTO(400, "Invalid parameter value for latitude");
        ErrorDTO actual = controllerExceptionHandler.handleMethodArgumentTypeMismatchException(e, null);

        assertEquals(expected.message(), actual.message());
    }
}