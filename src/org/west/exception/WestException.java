package org.west.exception;

public class WestException extends Exception {

    public WestException() {
    }

    public WestException(String message) {
        super(message);
    }

    public WestException(Throwable cause) {
        super(cause);
    }

    public WestException(String message, Throwable cause) {
        super(message, cause);
    }
}
