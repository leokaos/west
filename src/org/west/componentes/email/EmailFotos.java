package org.west.componentes.email;

import java.text.MessageFormat;
import org.west.entidades.Imovel;
import org.west.entidades.Usuario;
import org.west.exception.SendEmailException;

import static org.west.componentes.email.EmailUtil.adicionarQuebraDeLinha;

public class EmailFotos extends AbstractEmail {

    private Imovel imovel;
    private Integer aceitos;
    private Integer recusados;
    private final static String HEADER = "Aviso de Inclusão de Fotos - NÃO RESPONDER A ESTE E-MAIL";

    public EmailFotos(Imovel imovel, Integer aceitos, Integer recusados) {
        this.imovel = imovel;
        this.aceitos = aceitos;
        this.recusados = recusados;
    }

    @Override
    public String getHeaderText() {
        return HEADER;
    }

    @Override
    public String getBodyMessage() {
        StringBuilder builder = new StringBuilder();

        builder.append("Fotos adicionadas ao imovel de referência: {0}");
        adicionarQuebraDeLinha(builder, 2);
        builder.append("Endereço: {1}");
        adicionarQuebraDeLinha(builder, 3);
        builder.append("Sumário de Arquivos:");
        adicionarQuebraDeLinha(builder, 2);
        builder.append("Aceitos: {2}");
        adicionarQuebraDeLinha(builder);
        builder.append("Recusados: {3}");
        adicionarQuebraDeLinha(builder);

        return MessageFormat.format(builder.toString(), imovel.getReferencia(), imovel.getEndereco(), aceitos, recusados);
    }

    @Override
    public String getSubjectText() {
        return HEADER;
    }

    @Override
    public void setRecipients() throws SendEmailException {
        addRecipient("enio@westguerra.com.br");

        for (Usuario usuarioImovel : imovel.getUsuarios()) {
            addRecipient(usuarioImovel.getEmail());
        }
    }
}
