package org.west.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class NumeroPK implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "imovel", referencedColumnName = "referencia")
    private Imovel imovel;
    @Basic(optional = false)
    @Column(name = "numero")
    private Long numero;

    public NumeroPK() {
    }

    public NumeroPK(Imovel imovel, long numero) {
        this.imovel = imovel;
        this.numero = numero;
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
