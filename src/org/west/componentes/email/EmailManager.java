package org.west.componentes.email;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;

public class EmailManager {

    private static final String HOST_NAME = "smtp.westguerra.com.br";
    private static final String EMAIL_SEND = "suporte@westguerra.com.br";
    private static final String SENHA_EMAIL = "west1234";
    private static final Integer PORT_SMTP = 587;

    public static void sendEmail(AbstractEmail email) throws EmailException {
        try {
            email.setHostName(HOST_NAME);
            email.setSmtpPort(PORT_SMTP);
            email.setFrom(EMAIL_SEND);
            email.setAuthenticator(new DefaultAuthenticator(EMAIL_SEND, SENHA_EMAIL));
            email.contruir();

            email.send();
        } catch (Exception ex) {
            throw new EmailException(ex);
        }

    }
}