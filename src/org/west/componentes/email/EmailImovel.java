package org.west.componentes.email;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import org.west.entidades.Altera;
import org.west.entidades.Imovel;
import org.west.entidades.TipoAlteracao;
import org.west.entidades.Usuario;
import org.west.entidades.UsuarioDAO;
import org.west.exception.SendEmailException;

import static org.west.componentes.email.EmailUtil.adicionarQuebraDeLinha;

public class EmailImovel extends AbstractEmail {

    private static final String HEADER = "Aviso de Modificação de Imóvel - NÃO RESPONDER A ESTE E-MAIL";
    private Imovel imovel;
    private List<Altera> listaAltercacoes;

    public EmailImovel(Imovel imovel, List<Altera> listaAltercacoes) {
        this.imovel = imovel;
        this.listaAltercacoes = listaAltercacoes;
    }

    @Override
    public String getHeaderText() {
        return HEADER;
    }

    @Override
    public String getBodyMessage() {

        StringBuilder builder = new StringBuilder();

        builder.append("O imóvel de Referência ").append(imovel.getReferencia()).append(", ").append(imovel.getEnderecoCompleto()).append(" foi alterado:");
        builder.append(adicionarQuebraDeLinha(builder, 3));

        for (Altera altera : listaAltercacoes) {

            if (altera.getTipoAlteracao().equals(TipoAlteracao.DIVULGAR)) {
                builder.append("Divulgar alterado para: ").append((imovel.getDivulgar() ? "Divulgar." : "Não Divulgar."));
            } else if (altera.getTipoAlteracao().equals(TipoAlteracao.STATUS)) {
                builder.append("Status modificado para: ").append(imovel.getStatus()).append(".");
            } else {
                builder.append("Valor modificado para: ").append(getValorFormatado(imovel.getValor())).append(" (").append(altera.getDescricao()).append(").");
            }

            builder.append(adicionarQuebraDeLinha(builder, 2));
        }

        return builder.toString();
    }

    @Override
    public String getSubjectText() {
        return HEADER;
    }

    @Override
    public void setRecipients() throws SendEmailException {
        Usuario supervisor = UsuarioDAO.get(10l);
        addRecipient(supervisor.getEmail());
    }

    private String getValorFormatado(Double valor) {
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valor);
    }
}
