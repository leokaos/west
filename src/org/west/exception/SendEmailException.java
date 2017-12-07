package org.west.exception;

/**
 * Classe que encapsula um erro de e-mail.
 *
 * @author West Guerra Ltda.
 */
public class SendEmailException extends WestException {

    public SendEmailException() {
    }

    public SendEmailException(String message) {
        super(message);
    }

    public SendEmailException(Throwable cause) {
        super(cause);
    }

    public SendEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
