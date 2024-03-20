package cz.woidig.backend.exceptions;

public class GeoException extends RuntimeException {
    public GeoException(String msg) {
        super(msg);
    }

    public GeoException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
