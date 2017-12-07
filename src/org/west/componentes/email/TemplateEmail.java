package org.west.componentes.email;

import org.west.exception.SendEmailException;

/**
 * Interface que define um objeto de envio de email.
 *
 * @author West Guerra Ltda.
 */
public interface TemplateEmail {

    public String getHeaderText();

    public String getBodyMessage();

    public String getFooterText();

    public String getSubjectText();

    public void setRecipients() throws SendEmailException;
}
