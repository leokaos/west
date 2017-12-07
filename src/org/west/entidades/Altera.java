package org.west.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "tab_altera")
public class Altera implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "codigo", nullable = false)
    private Long codigo;
    @Column(name = "descricao", nullable = false)
    private String descricao;
    @ManyToOne(targetEntity = Imovel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "imovel", referencedColumnName = "referencia")
    @Cascade(CascadeType.LOCK)
    private Imovel imovel;

    public Altera() {
    }

    public Altera(Long codigo) {
        this.codigo = codigo;
    }

    public Altera(Long codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Altera)) {
            return false;
        }
        Altera other = (Altera) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.west.entidades.Altera[ codigo=" + codigo + " ]";
    }

    public TipoAlteracao getTipoAlteracao() {
        return TipoAlteracao.parse(descricao);
    }

    public Altera duplicar() {
        Altera altera = new Altera();

        altera.setCodigo(getCodigo());
        altera.setDescricao(getDescricao());
        altera.setImovel(getImovel());

        return altera;
    }
}