package org.west.exception;

public class ExportException extends WestException{
    
    public ExportException() {
    }

    public ExportException(String message) {
        super(message);
    }

    public ExportException(Throwable cause) {
        super(cause);
    }
    
    public ExportException(String message, Throwable cause) {
        super(message, cause);
    }
}