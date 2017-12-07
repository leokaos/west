package org.west.entidades;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tab_numero")
public class Numero implements Serializable {

    @EmbeddedId
    protected NumeroPK numeroPK;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "imovel", referencedColumnName = "referencia", insertable = false, updatable = false)
    private Imovel imovel;
    @Column(name = "numero", insertable = false, updatable = false)
    private Long numero;

    public Numero() {
    }

    public Numero(Imovel imovel, Long numero) {
        this.numeroPK = new NumeroPK(imovel, numero);
    }

    public NumeroPK getNumeroPK() {
        return numeroPK;
    }

    public void setNumeroPK(NumeroPK numeroPK) {
        this.numeroPK = numeroPK;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }
}
