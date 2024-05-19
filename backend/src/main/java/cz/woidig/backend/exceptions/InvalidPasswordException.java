package cz.woidig.backend.exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String msg) {
        super(msg);
    }

    public InvalidPasswordException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
