package cz.woidig.backend.controller;

import cz.woidig.backend.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNoHandlerFound(NoHandlerFoundException e, WebRequest request) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), "Not found");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleMissingServletRequestParameter(MissingServletRequestParameterException e, WebRequest request) {
        return new ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Missing request parameter: " + e.getParameterName());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleIllegalArgumentException(IllegalArgumentException e, WebRequest request) {
        return new ErrorDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleUnhandledException(Exception e, WebRequest request) {
        log.error("unhandled exception", e);
        return new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
    }
}
