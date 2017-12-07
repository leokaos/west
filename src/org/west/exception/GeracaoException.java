package org.west.exception;

public class GeracaoException extends WestException{

    public GeracaoException() {
    }

    public GeracaoException(String message) {
        super(message);
    }

    public GeracaoException(Throwable cause) {
        super(cause);
    }

    public GeracaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
