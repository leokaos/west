package org.west.entidades;



import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *Classe de persistência referente à tabela tab_cep.
 * @author WestGuerra Ltda.
 */
@Entity
@Table(name="tab_cep")
public class Cep implements Serializable, Comparable<Cep> {
    
    @Id
    @Column(name="CEP",length=8,nullable=false)
    private String CEP;
    @Column(name="uf",length=2,nullable=false)
    private String uf;
    @Column(name="tipo",length=100,nullable=false)
    private String tipo;
    @Column(name="cidade",length=255,nullable=false)
    private String cidade;
    @Column(name="descricao",length=100,nullable=false)
    private String descricao;

    public Cep() {
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (!(obj instanceof Cep))
            return false;
               
        Cep cep = (Cep) obj;
        
        if ((getCEP() != null && !getCEP().equals(cep.getCEP())) || (getCEP() == null && cep.getCEP() != null))
                return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + (this.CEP != null ? this.CEP.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return String.valueOf(getTipo() + " " + getDescricao());
    }

    @Override
    public int compareTo(Cep o) {
        return getDescricao().compareTo(o.getDescricao());
    }
}