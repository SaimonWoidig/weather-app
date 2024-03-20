package cz.woidig.backend.exceptions;

public class WeatherException extends RuntimeException {
    public WeatherException(String msg) {
        super(msg);
    }

    public WeatherException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
