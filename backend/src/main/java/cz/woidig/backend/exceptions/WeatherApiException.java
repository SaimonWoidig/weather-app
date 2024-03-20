package cz.woidig.backend.exceptions;

public class WeatherApiException extends Exception {
    public WeatherApiException(String msg) {
        super(msg);
    }

    public WeatherApiException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
