package org.west.componentes.email;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.west.componentes.DesktopSession;
import org.west.entidades.Usuario;
import org.west.entidades.WestPersistentManager;
import org.west.exception.SendEmailException;

import static org.west.componentes.email.EmailUtil.adicionarQuebraDeLinha;

public abstract class AbstractEmail extends HtmlEmail implements TemplateEmail {

    private static final String TEMPLATE = "/template.html";
    protected Usuario usuario;
    private Date dataServer;

    public AbstractEmail() {
        this.usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");
        this.dataServer = WestPersistentManager.getDateServer();
        setCharset("UTF-8");
    }

    @Override
    public String getFooterText() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        StringBuilder builder = new StringBuilder();

        adicionarQuebraDeLinha(builder, 3);

        builder.append("Mensagem gerada em ");
        builder.append(format.format(dataServer));
        builder.append(" com o usuário ");
        builder.append(usuario.getNome());
        builder.append(" logado.");

        return builder.toString();
    }

    protected void contruir() throws SendEmailException {
        String template = getTemplateHTML();

        template = MessageFormat.format(template, new Object[]{getHeaderText(), getBodyMessage(), getFooterText()});
        setRecipients();

        try {
            this.setHtmlMsg(template);
        } catch (Exception ex) {
            throw new SendEmailException(ex);
        }
    }

    private String getTemplateHTML() throws SendEmailException {
        try {
            return IOUtils.toString(getClass().getResourceAsStream(TEMPLATE));
        } catch (Exception ex) {
            throw new SendEmailException(ex);
        }
    }

    protected void addRecipient(String recipient) throws SendEmailException {
        try {
            addTo(recipient);
        } catch (EmailException ex) {
            throw new SendEmailException(ex);
        }
    }
}
