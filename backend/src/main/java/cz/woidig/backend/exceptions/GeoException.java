package cz.woidig.backend.exceptions;

public class GeoException extends Exception {
    public GeoException(String msg) {
        super(msg);
    }

    public GeoException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
