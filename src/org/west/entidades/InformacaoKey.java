package org.west.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Classe de persistência referente à Chave Primária da tabela tab_informacoes.
 *
 * @author WestGuerra Ltda.
 */
@Embeddable
public class InformacaoKey implements Serializable {

    @Column(name = "referencia", length = 20, nullable = false)
    private Long referencia;

    @Column(name = "anuncio", length = 100, nullable = false)
    private String anuncio;

    public Long getReferencia() {
        return referencia;
    }

    public void setReferencia(Long referencia) {
        this.referencia = referencia;
    }

    public String getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(String anuncio) {
        this.anuncio = anuncio;
    }
}