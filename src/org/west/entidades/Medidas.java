package org.west.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import static org.west.utilitarios.ValidadorUtil.*;

/**
 *
 * @author leo
 */
@Entity
@Table(name = "tab_medidas_imovel")
public class Medidas implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "frente")
    private Double frente;
    @Column(name = "esquerda")
    private Double esquerda;
    @Column(name = "traseira")
    private Double traseira;
    @Column(name = "direita")
    private Double direita;
    @OneToOne(targetEntity = Imovel.class, fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.LOCK})
    @JoinColumns(
    @JoinColumn(name = "imovel_id"))
    private Imovel imovel;

    public Medidas() {
    }

    public Medidas(Imovel imovel) {
        this.imovel = imovel;
    }

    public Medidas(Long id) {
        this.id = id;
    }

    public Double getFrente() {
        return frente;
    }

    public void setFrente(Double frente) {
        this.frente = frente;
    }

    public Double getEsquerda() {
        return esquerda;
    }

    public void setEsquerda(Double esquerda) {
        this.esquerda = esquerda;
    }

    public Double getTraseira() {
        return traseira;
    }

    public void setTraseira(Double traseira) {
        this.traseira = traseira;
    }

    public Double getDireita() {
        return direita;
    }

    public void setDireita(Double direita) {
        this.direita = direita;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Medidas)) {
            return false;
        }
        Medidas other = (Medidas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public boolean isVazio() {
        return isEmpty(frente) && isEmpty(esquerda) && isEmpty(traseira) && isEmpty(direita);
    }

    @Override
    public String toString() {
        return frente + " - " + esquerda + " - " + direita + " - " + traseira;
    }
}
